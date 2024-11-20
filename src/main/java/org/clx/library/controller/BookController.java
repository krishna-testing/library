package org.clx.library.controller;

import org.clx.library.exception.AuthorNotFoundException;
import org.clx.library.exception.BookNotFoundException;
import org.clx.library.model.Book;
import org.clx.library.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class); // Logger initialization

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Create a new Book
    @PostMapping("/createBook")
    public ResponseEntity<String> createBook(@RequestBody Book book, @RequestParam Integer authorId) {
        logger.info("Received request to create book with authorId: {}", authorId);
        try {
            Book createdBook = bookService.createBook(book, authorId);
            logger.info("Book created successfully with ID: {}", createdBook.getId());
            return new ResponseEntity<>("Book created with ID: " + createdBook.getId(), HttpStatus.CREATED);
        } catch (AuthorNotFoundException ex) {
            logger.error("Failed to create book: Author with ID {} not found", authorId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error while creating book: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating book");
        }
    }

    // Delete a Book by ID (and ensure it's the author's book)
    @DeleteMapping("/deleteBook")
    public ResponseEntity<String> deleteBook(@RequestParam Integer bookId, @RequestParam Integer authorId) {
        logger.info("Received request to delete book with ID: {} for authorId: {}", bookId, authorId);
        try {
            String message = bookService.deleteBook(bookId, authorId);
            logger.info("Book with ID: {} deleted successfully", bookId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to delete book with ID: {}: {}", bookId, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get all books
    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) String author) {
        logger.info("Received request to get books with parameters - Genre: {}, Available: {}, Author: {}", genre, isAvailable, author);
        List<Book> books = bookService.getBooks(genre, isAvailable, author);
        logger.info("Returning {} books", books.size());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Get book by ID
    @GetMapping("/getBook/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        logger.info("Received request to get book with ID: {}", id);
        try {
            Book book = bookService.findBookById(id);
            logger.info("Book with ID: {} found", id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            logger.error("Book with ID: {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching book with ID: {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Save/Remove a book from an author's saved books
    @PutMapping("/saveBook/{bookId}")
    public ResponseEntity<Book> saveBook(@PathVariable Integer bookId, @RequestParam Integer authorId) {
        logger.info("Received request to save book with ID: {} for authorId: {}", bookId, authorId);
        try {
            Book updatedBook = bookService.savedBook(bookId, authorId);
            logger.info("Book with ID: {} saved successfully", bookId);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to save book with ID: {}: {}", bookId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
