package com.esab.springauth.dtos.authors;

import java.util.List;

import com.esab.springauth.dtos.books.BookDTO;
import com.esab.springauth.dtos.users.UserDTO;

public record AuthorDTO(Long id, String name, String nationality, List<BookDTO> books, UserDTO createdBy) {
}
