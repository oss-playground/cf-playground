package com.aruna.samples.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aruna.samples.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
	
	 public List<Book> findByTitle(String title);

}
