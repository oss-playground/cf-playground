package com.aruna.samples.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

//	static final Histogram requestLatency = Histogram.build()
//			.name("requests_latency_seconds").help("Request latency in seconds.").register();

	@GetMapping("/books")
	public List<Book> getAllBooks() {




		return (List<Book>) bookRepo.findAll();
	}

	@PostMapping("/books")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createBook(@Valid @RequestBody Book book, UriComponentsBuilder uriBuilder) {
		Book savedBook = bookRepo.save(book);
		//TODO : To be replaced with logging here
		System.out.println("book saved is " + savedBook);
		URI location = uriBuilder.path("/api/v1/books/{id}").buildAndExpand(savedBook.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/books/title/{bookTitle}")
	public ResponseEntity<Object> findByTitle(@PathVariable String bookTitle) {
		List<Book> books = bookRepo.findByTitle(bookTitle);
		if (books != null && books.size() > 0) {
			return ResponseEntity.ok(bookRepo.findByTitle(bookTitle));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/books/{id}")
	public ResponseEntity<Book> findByIdentity(@PathVariable Long id) {
		Optional<Book> bookOpt = Optional.ofNullable(bookRepo.findOne(id));

		if (bookOpt.isPresent()) {
			return ResponseEntity.ok(bookOpt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/books/{id}")
	public ResponseEntity<Object> updateStudent(@RequestBody Book book, @PathVariable long id) {

		Optional<Book> bookOpt = Optional.ofNullable(bookRepo.findOne(id));

		if (!bookOpt.isPresent())
			return ResponseEntity.notFound().build();

		bookRepo.save(book);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/books/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable long id) {
		Book book = bookRepo.findOne(id);

		if (book == null) {
			return ResponseEntity.notFound().build();
		} 

		bookRepo.delete(book);

		return ResponseEntity.ok().build();
	}

}
