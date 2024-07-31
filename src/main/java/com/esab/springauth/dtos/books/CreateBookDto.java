package com.esab.springauth.dtos.books;

public record CreateBookDto(
        String name,
        String USBN,
        int pagesNumber,
        long author_id) {
}
