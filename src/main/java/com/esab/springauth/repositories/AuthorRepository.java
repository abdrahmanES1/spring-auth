package com.esab.springauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esab.springauth.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByName(String name);

}
