package com.esab.springauth.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.esab.springauth.dtos.authors.AuthorDTO;
import com.esab.springauth.dtos.books.BookDTO;
import com.esab.springauth.dtos.users.UserDTO;
import com.esab.springauth.entities.Author;
import com.esab.springauth.entities.Book;
import com.esab.springauth.entities.User;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    // Maps Author entity to AuthorDTO
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "books", source = "books")
    AuthorDTO toAuthorDTO(Author author);

    // Maps Book entity to BookDTO
    @Mapping(target = "createdBy", source = "createdBy")
    BookDTO toBookDTO(Book book);

    // Maps User entity to UserDTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toUserDTO(User user);
}