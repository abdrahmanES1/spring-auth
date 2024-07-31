package com.esab.springauth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.esab.springauth.dtos.authors.AuthorDTO;
import com.esab.springauth.dtos.books.BookDTO;
import com.esab.springauth.dtos.users.UserDTO;
import com.esab.springauth.entities.Author;
import com.esab.springauth.entities.Book;
import com.esab.springauth.entities.User;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "createdBy", source = "createdBy")
    BookDTO toBookDTO(Book book);

    // If you need to map back from DTO to Entity
    @Mapping(target = "createdBy", source = "createdBy")
    Book toBook(BookDTO bookDTO);

    // Maps User entity to UserDTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toUserDTO(User user);

    // Maps User entity to UserDTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "nationality", source = "nationality")
    AuthorDTO toAuthorDTO(Author author);

}
