package com.example.graphqldemo.service;

import com.example.graphqldemo.config.MDCContextLifter;
import com.example.graphqldemo.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final R2dbcEntityTemplate template;

    public BookService(R2dbcEntityTemplate template) {
        this.template = template;
    }

    public Flux<Book> getAllBooks() {
        logger.info("Fetching all books");
        return template.select(Book.class)
                .all()
                .doOnNext(book -> logger.info("Found book: {}", book))
                .doOnComplete(() -> logger.info("Completed fetching all books"))
                .doOnEach(MDCContextLifter.liftContext());
    }

    public Mono<Book> getBookById(Long id) {
        logger.info("Fetching book with id: {}", id);
        return template.select(Book.class)
                .matching(Query.query(Criteria.where("id").is(id)))
                .one()
                .doOnNext(book -> logger.info("Found book: {}", book))
                .switchIfEmpty(Mono.error(new RuntimeException("Book not found with id: " + id)))
                .doOnError(error -> logger.error("Error fetching book with id {}: {}", id, error.getMessage()))
                .doOnEach(MDCContextLifter.liftContext());
    }

    public Mono<Book> saveBook(Book book) {
        logger.info("Saving new book: {}", book);
        return template.insert(Book.class)
                .using(book)
                .doOnNext(savedBook -> logger.info("Successfully saved book: {}", savedBook))
                .doOnError(error -> logger.error("Error saving book: {}", error.getMessage()))
                .doOnEach(MDCContextLifter.liftContext());
    }

    public Mono<Book> updateBook(Long id, Book book) {
        logger.info("Updating book with id {}: {}", id, book);
        return template.update(Book.class)
                .matching(Query.query(Criteria.where("id").is(id)))
                .apply(Update.update("title", book.getTitle())
                        .set("author", book.getAuthor())
                        .set("pageCount", book.getPageCount()))
                .then(getBookById(id))
                .doOnNext(updatedBook -> logger.info("Successfully updated book: {}", updatedBook))
                .doOnError(error -> logger.error("Error updating book with id {}: {}", id, error.getMessage()))
                .doOnEach(MDCContextLifter.liftContext());
    }

    public Mono<Void> deleteBook(Long id) {
        logger.info("Deleting book with id: {}", id);
        return template.delete(Book.class)
                .matching(Query.query(Criteria.where("id").is(id)))
                .all()
                .then()
                .doOnSuccess(v -> logger.info("Successfully deleted book with id: {}", id))
                .doOnError(error -> logger.error("Error deleting book with id {}: {}", id, error.getMessage()))
                .doOnEach(MDCContextLifter.liftContext());
    }

    public Flux<Book> findBooksByAuthor(String author) {
        logger.info("Searching books by author: {}", author);
        return template.select(Book.class)
                .matching(Query.query(Criteria.where("author").like("%" + author + "%")))
                .all()
                .doOnNext(book -> logger.info("Found book by author: {}", book))
                .doOnComplete(() -> logger.info("Completed searching books by author: {}", author))
                .doOnEach(MDCContextLifter.liftContext());
    }
} 