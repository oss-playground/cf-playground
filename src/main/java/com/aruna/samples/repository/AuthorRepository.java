package com.aruna.samples.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aruna.samples.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
	
	public List<Author> findByName(String name);

}
