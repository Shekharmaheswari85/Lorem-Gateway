package com.example.graphqldemo.config;

import com.example.graphqldemo.model.Book;
import com.example.graphqldemo.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(BookService bookService) {
        return args -> {
            Flux.just(
                new Book(null, "The Pragmatic Programmer", "Andy Hunt", 320),
                new Book(null, "Clean Code", "Robert C. Martin", 464),
                new Book(null, "Design Patterns", "Erich Gamma", 395),
                new Book(null, "Effective Java", "Joshua Bloch", 412)
            )
            .flatMap(bookService::saveBook)
            .subscribe();
        };
    }
} 