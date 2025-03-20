package com.example.graphqldemo.resolver;

import com.example.graphqldemo.model.Book;
import com.example.graphqldemo.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class BookResolver {

    private final BookService bookService;

    public BookResolver(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public Flux<Book> books() {
        return bookService.getAllBooks();
    }

    @QueryMapping
    public Mono<Book> book(@Argument Long id) {
        return bookService.getBookById(id);
    }

    @QueryMapping
    public Flux<Book> booksByAuthor(@Argument String author) {
        return bookService.findBooksByAuthor(author);
    }

    @MutationMapping
    public Mono<Book> addBook(@Argument String title, @Argument String author, @Argument Integer pageCount) {
        Book book = new Book(null, title, author, pageCount);
        return bookService.saveBook(book);
    }

    @MutationMapping
    public Mono<Book> updateBook(@Argument Long id, @Argument String title, 
                               @Argument String author, @Argument Integer pageCount) {
        Book book = new Book(id, title, author, pageCount);
        return bookService.updateBook(id, book);
    }

    @MutationMapping
    public Mono<Boolean> deleteBook(@Argument Long id) {
        return bookService.deleteBook(id)
                .thenReturn(true)
                .onErrorReturn(false);
    }
} 