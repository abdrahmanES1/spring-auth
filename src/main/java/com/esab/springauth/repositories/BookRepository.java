package com.esab.springauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

import com.esab.springauth.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByName(String name);

}
