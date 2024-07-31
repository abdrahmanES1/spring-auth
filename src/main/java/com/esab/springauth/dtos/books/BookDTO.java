package com.esab.springauth.dtos.books;

import com.esab.springauth.dtos.users.UserDTO;

public record BookDTO(Long id, String name, int pagesNumber, String USBN, UserDTO createdBy) {}
