package org.clx.library.controller;

import lombok.extern.slf4j.Slf4j;
import org.clx.library.exception.AuthorNotFoundException;
import org.clx.library.exception.BookNotFoundException;
import org.clx.library.model.Book;
import org.clx.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Create a new Book
    @PostMapping("/createBook")
    public ResponseEntity<String> createBook(@RequestBody Book book, @RequestParam Integer authorId) {
        log.info("Received request to create book with authorId: {}", authorId);
        try {
            Book createdBook = bookService.createBook(book, authorId);
            log.info("Book created successfully with ID: {}", createdBook.getId());
            return new ResponseEntity<>("Book created with ID: " + createdBook.getId(), HttpStatus.CREATED);
        }catch (AuthorNotFoundException ex){
            log.error("Failed to create book: Author with ID {} not found", authorId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }

    // Delete a Book by ID (and ensure it's the author's book)
    @DeleteMapping("/deleteBook")
    public ResponseEntity<String> deleteBook(@RequestParam Integer bookId, @RequestParam Integer authorId) {
        log.info("Received request to delete book with ID: {} for authorId: {}", bookId, authorId);
        try {
            String message = bookService.deleteBook(bookId, authorId);
            log.info("Book with ID: {} deleted successfully", bookId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete book with ID: {}: {}", bookId, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get all books
    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false,defaultValue = "true") Boolean isAvailable,
            @RequestParam(required = false) String author) {
        log.info("Received request to get books with parameters - Genre: {}, Available: {}, Author: {}", genre, isAvailable, author);
        List<Book> books = bookService.getBooks(genre, isAvailable, author);
        log.info("Returning {} books", books.size());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Get book by ID
    @GetMapping("/getBook/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        log.info("Received request to get book with ID: {}", id);
        try {
            Book book = bookService.findBookById(id);
            log.info("Book with ID: {} found", id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            log.error("Book with ID: {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Save/Remove a book from an author's saved books
    @PutMapping("/saveBook/{bookId}")
    public ResponseEntity<Book> saveBook(@PathVariable Integer bookId, @RequestParam Integer authorId) {
        log.info("Received request to save book with ID: {} for authorId: {}", bookId, authorId);
        try {
            Book updatedBook = bookService.savedBook(bookId, authorId);
            log.info("Book with ID: {} saved successfully", bookId);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to save book with ID: {}: {}", bookId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
