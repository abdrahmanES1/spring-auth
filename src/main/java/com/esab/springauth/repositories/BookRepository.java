package com.esab.springauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esab.springauth.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByName(String name);

}
