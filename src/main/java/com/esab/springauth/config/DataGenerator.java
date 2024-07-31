package com.esab.springauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esab.springauth.entities.Author;
import com.esab.springauth.entities.Book;
import com.esab.springauth.entities.Privilege;
import com.esab.springauth.entities.Role;
import com.esab.springauth.entities.User;
import com.esab.springauth.repositories.AuthorRepository;
import com.esab.springauth.repositories.BookRepository;
import com.esab.springauth.repositories.PrivilegeRepository;
import com.esab.springauth.repositories.RoleRepository;
import com.esab.springauth.repositories.UserRepository;

import java.util.Arrays;

//@Component
public class DataGenerator implements CommandLineRunner {

        @Autowired
        private RoleRepository roleRepository;
        @Autowired
        private PrivilegeRepository privilageRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private BookRepository bookRepository;
        @Autowired
        private AuthorRepository authorRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        @Transactional
        public void run(String... args) throws Exception {

                Privilege ADMIN_ADD = privilageRepository.save(new Privilege("create:all"));
                Privilege ADMIN_EDIT = privilageRepository.save(new Privilege("update:all"));
                Privilege ADMIN_READ = privilageRepository.save(new Privilege("view:all"));
                Privilege ADMIN_DELETE = privilageRepository.save(new Privilege("delete:all"));

                Role ROLE_ADMIN = roleRepository.save(new Role("ROLE_ADMIN", Arrays.asList(
                                ADMIN_ADD,
                                ADMIN_EDIT,
                                ADMIN_READ,
                                ADMIN_DELETE)));

                Privilege viewBooks = privilageRepository.findByAuthority("view:books");

                Role ROLE_USER = roleRepository.save(new Role("ROLE_USER", Arrays.asList(viewBooks)));

                User user = new User("user", passwordEncoder.encode("password"), Arrays.asList(ROLE_USER));
                User admin = new User("admin", passwordEncoder.encode("password"), Arrays.asList(ROLE_ADMIN));

                userRepository.saveAll(Arrays.asList(user, admin));

                Author author = Author.builder()
                                .createdBy(user)
                                .name("author 1")
                                .nationality("moroccan")
                                .build();

                Author author1 = Author.builder()
                                .createdBy(user)
                                .name("author 2")
                                .nationality("spain")
                                .build();

                Author author2 = Author.builder()
                                .createdBy(user)
                                .name("author 3")
                                .nationality("moroccan")
                                .build();

                Book book = Book.builder()
                                .author(author)
                                .createdBy(admin)
                                .name("Book 4")
                                .USBN("USNB 123")
                                .pagesNumber(900)
                                .build();

                Book book1 = Book.builder()
                                .author(author)
                                .createdBy(admin)
                                .name("Book 2")
                                .USBN("USNB 231001")
                                .pagesNumber(190)
                                .build();

                Book book2 = Book.builder()
                                .createdBy(user)
                                .author(author1)
                                .name("Book 3")
                                .USBN("USNB 86289012")
                                .pagesNumber(200)
                                .build();

                authorRepository.saveAll(Arrays.asList(author, author1, author2));
                bookRepository.saveAll(Arrays.asList(book, book1, book2));

        }
}