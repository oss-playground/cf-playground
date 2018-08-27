package com.aruna.samples.rest;

import java.net.URI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.xml.ws.Response;

import ch.qos.logback.core.encoder.EchoEncoder;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import io.prometheus.client.SimpleCollector;
import io.prometheus.client.exporter.PushGateway;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.aruna.samples.model.Book;
import com.aruna.samples.repository.BookRepository;

@RestController
@RequestMapping("/api/v1")
public class BookService {

    private final Logger LOG = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BookRepository bookRepo;

    static Counter requests;

    static Histogram requestLatency;

    PushGateway pushGateway;

    public BookService() {
        pushGateway = new PushGateway("10.100.214.175:9091");

        // setting up the counter for all request endpoints
        requests = Counter.build().name("requests_count").help("Total Number of Requests for each endpoint").
                labelNames("endpoint", "type").register();

//        // setting up the histogram for request latency on all endpoints
//        requestLatency = Histogram.build().name("requests_latency_seconds").help("Request latency in seconds.")..register();
//        pushGateway.pushAdd(requestLatency, "latency_job");
    }

    @GetMapping("/books")
    @Timed(value = "books.retrieveAll", histogram = true, percentiles = {0.95, 0.99}, extraTags = {"environment", "dev"},
            description = "This is for retrieving all books from the API")
    public List<Book> getAllBooks() {
        try {
            requests.labels("books.retrieveAll", "GET").inc();
            pushGateway.pushAdd(requests, "requests_count");
            return (List<Book>) bookRepo.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    @Timed(value = "books.create", histogram = true, percentiles = {0.95, 0.99}, extraTags = {"environment", "dev"},
            description = "This is for the createBook API calls. Checking the description thing in Micrometer")
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book, UriComponentsBuilder uriBuilder) {
        try {
            Book savedBook = bookRepo.save(book);
            System.out.println("book saved is " + savedBook);

            // first the count of the requests
            requests.labels("books.create", "POST").inc();
            pushGateway.pushAdd(requests, "requests_count");

            URI location = uriBuilder.path("/api/v1/books/{id}").buildAndExpand(savedBook.getId()).toUri();
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }

    @GetMapping("/books/title/{bookTitle}")
    public ResponseEntity<Object> findByTitle(@PathVariable String bookTitle) {
        try {
            List<Book> books = bookRepo.findByTitle(bookTitle);

            // count of get books by title
            requests.labels("books.title", "GET").inc();
            pushGateway.pushAdd(requests, "requests_count");

            if (books != null && books.size() > 0) {
                return ResponseEntity.ok(bookRepo.findByTitle(bookTitle));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> findByIdentity(@PathVariable Long id) {
        try {
            Optional<Book> bookOpt = Optional.ofNullable(bookRepo.findOne(id));

            requests.labels("books.id", "GET").inc();
            pushGateway.pushAdd(requests, "requests_count");

            if (bookOpt.isPresent()) {
                return ResponseEntity.ok(bookOpt.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Book book, @PathVariable long id) {
        try {
            Optional<Book> bookOpt = Optional.ofNullable(bookRepo.findOne(id));

            requests.labels("books.id", "PUT").inc();
            pushGateway.pushAdd(requests, "requests_count");

            if (!bookOpt.isPresent())
                return ResponseEntity.notFound().build();

            bookRepo.save(book);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable long id) {
        try {
            Book book = bookRepo.findOne(id);

            requests.labels("books.id", "DELETE").inc();
            pushGateway.pushAdd(requests, "requests_count");

            if (book == null) {
                return ResponseEntity.notFound().build();
            }

            bookRepo.delete(book);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
