package com.esab.springauth.controllers;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esab.springauth.dtos.books.CreateBookDto;
import com.esab.springauth.entities.Author;
import com.esab.springauth.entities.Book;
import com.esab.springauth.mapper.BookMapper;
import com.esab.springauth.repositories.AuthorRepository;
import com.esab.springauth.repositories.BookRepository;
import com.esab.springauth.responses.GenericResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

  private final AuthorRepository authorRepository;
  private final BookRepository bookrepository;
  private final BookMapper bookMapper;

  @GetMapping
  @PreAuthorize("hasAuthority('view:books')")
  @Cacheable("books")
  public ResponseEntity<GenericResponse<List<Book>>> findAll() {
    var books = bookrepository.findAll()
        .stream()
        .map(book -> bookMapper.toBookDTO(book))
        .collect(Collectors.toList());

    GenericResponse response = GenericResponse.builder()
        .message("fetched books success")
        .status(HttpStatus.OK.value())
        .data(books)
        .build();

    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(Duration.ofMinutes(10))) // Cache for 10 minutes
        .body(response);

  }

  @PostMapping
  public ResponseEntity<GenericResponse<Book>> create(@RequestBody @Valid CreateBookDto data) {
    Optional<Author> author = authorRepository.findById(data.author_id());
    if (!author.isPresent()) {
      throw new RuntimeException("Author not Found");
    }
    Book book = Book.builder()
        .pagesNumber(data.pagesNumber())
        .author(author.get())
        .USBN(data.USBN())
        .name(data.name())
        .build();

    bookrepository.save(book);

    GenericResponse response = GenericResponse.builder()
        .message("fetched book success")
        .status(HttpStatus.OK.value())
        .data(book)
        .build();
    return ResponseEntity.ok().body(response);
  }

}