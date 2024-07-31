package com.esab.springauth.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esab.springauth.dtos.authors.CreateAuthorDto;
import com.esab.springauth.entities.Author;
import com.esab.springauth.entities.Book;
import com.esab.springauth.mapper.AuthorMapper;
import com.esab.springauth.repositories.AuthorRepository;
import com.esab.springauth.responses.GenericResponse;

import jakarta.validation.Valid;
import jakarta.websocket.MessageHandler.Partial;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/authors")
public class AuthorController {

  final private AuthorRepository authorRepository;
  final private AuthorMapper authorMapper;

  @GetMapping
  public ResponseEntity<GenericResponse<List<Author>>> findAll() {
    var authors = authorRepository.findAll().stream().map(author -> authorMapper.toAuthorDTO(author))
        .collect(Collectors.toList());
    GenericResponse response = GenericResponse.builder()
        .message("fetched book success")
        .status(HttpStatus.OK.value())
        .data(authors)
        .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GenericResponse<List<Author>>> findOne(@PathVariable("id") Long id) {

    Optional<Author> author = authorRepository.findById(id);
    if (!author.isPresent()) {
      throw new RuntimeException("Author not found");
    }

    GenericResponse response = GenericResponse.builder()
        .message("fetched book success")
        .status(HttpStatus.OK.value())
        .data(author)
        .build();
    return ResponseEntity.ok().body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<GenericResponse<List<Author>>> update(@PathVariable("id") Long id,
      @RequestBody @Valid Partial<CreateAuthorDto> data) {

    Optional<Author> author = authorRepository.findById(id);
    if (!author.isPresent()) {
      throw new RuntimeException("Author not found");
    }

    GenericResponse response = GenericResponse.builder()
        .message("fetched book success")
        .status(HttpStatus.OK.value())
        .data(author)
        .build();
    return ResponseEntity.ok().body(response);
  }

  @PostMapping
  public ResponseEntity<GenericResponse<Book>> create(@RequestBody @Valid CreateAuthorDto data) {

    Author author = Author.builder()
        .name(data.getName())
        .nationality(data.getNationality())
        .build();
    authorRepository.save(author);

    GenericResponse response = GenericResponse.builder()
        .message("fetched author success")
        .status(HttpStatus.OK.value())
        .data(author)
        .build();
    return ResponseEntity.ok().body(response);
  }

}