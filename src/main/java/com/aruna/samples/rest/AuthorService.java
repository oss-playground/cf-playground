package com.aruna.samples.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import com.aruna.samples.model.Author;
import com.aruna.samples.model.Book;
import com.aruna.samples.repository.AuthorRepository;

@RestController
@RequestMapping("/api/v1")
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepo;

	@GetMapping("/authors")
	public List<Author> getAllAuthors() {
		return (List<Author>) authorRepo.findAll();
	}

	@PostMapping("/authors")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createNote(@Valid @RequestBody Author author, UriComponentsBuilder uriBuilder) {
		Author savedAuthor = authorRepo.save(author);
		//TODO : To be replaced with logging here
		System.out.println("Author saved is " + savedAuthor);
		URI location = uriBuilder.path("/api/v1/authors/{id}").buildAndExpand(savedAuthor.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/authors/{authorName}/books")
	public ResponseEntity<Set<String>> findByName(@PathVariable String authorName) {
		List<Author> authors = authorRepo.findByName(authorName); 
		if (authors != null) {
			Set<Book> books = authors.get(0).getBooks();
			Set<String> titles = books.stream().map(book -> book.getTitle()).collect(Collectors.toSet());
			System.out.println(String.format("Books by author :  %s are %s", authors.get(0).getName(), titles));
			return ResponseEntity.ok(titles);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/authors/{id}")
	public ResponseEntity<Author> findByIdentity(@PathVariable Long id) {
		Optional<Author> authorOpt = Optional.ofNullable(authorRepo.findOne(id));
		if (authorOpt.isPresent()) {
			return ResponseEntity.ok(authorOpt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/authors/{id}/books")
	public ResponseEntity<Set<String>> findBooksByIdentity(@PathVariable Long id) {
		Author authr = authorRepo.findOne(id);
		if (authr != null) {
			Set<Book> books = authr.getBooks();
			Set<String> titles = books.stream().map(book -> book.getTitle()).collect(Collectors.toSet());
			System.out.println(String.format("Books by author :  %s are %s", authr.getName(), titles));
			return ResponseEntity.ok(titles);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/authors/{id}")
	public ResponseEntity<Object> updateStudent(@RequestBody Author author, @PathVariable long id) {

		Optional<Author> authorOpt = Optional.ofNullable(authorRepo.findOne(id));

		if (!authorOpt.isPresent())
			return ResponseEntity.notFound().build();

		authorRepo.save(author);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/authors/{id}")
	public ResponseEntity<?> deleteAuthor(@PathVariable long id) {
		Author author = authorRepo.findOne(id);

		if (author == null) {
			return ResponseEntity.notFound().build();
		} 

		authorRepo.delete(author);

		return ResponseEntity.ok().build();
	}

}
