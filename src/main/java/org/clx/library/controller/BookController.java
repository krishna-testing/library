package org.clx.library.controller;

import lombok.extern.slf4j.Slf4j;
import org.clx.library.dto.BookDto;
import org.clx.library.exception.AuthorNotFoundException;
import org.clx.library.exception.BookNotFoundException;
import org.clx.library.exception.ResourceNotFoundException;
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
    @PostMapping("/authors/{authorId}/books")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto, @PathVariable int authorId) {
        log.info("Received request to create book with authorId: {}, BookDto: {}", authorId, bookDto);

        try {
            BookDto createBook = this.bookService.createBook(bookDto, authorId);
            log.info("Book created successfully with ID: {}", createBook.getId());
            return new ResponseEntity<>(createBook, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create book with authorId: {}, BookDto: {}. Error: {}", authorId, bookDto, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
            log.error("Failed to delete book with ID: {}. Error: {}", bookId, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get all books
    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false, defaultValue = "true") Boolean isAvailable,
            @RequestParam(required = false) String author) {
        log.info("Received request to get books with parameters - Genre: {}, Available: {}, Author: {}", genre, isAvailable, author);
        try {
            List<Book> books = bookService.getBooks(genre, isAvailable, author);
            log.info("Returning {} books", books.size());
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to retrieve books. Error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a book by its ID
    @GetMapping("/posts/{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Integer bookId) {
        log.info("Received request to get book with ID: {}", bookId);
        try {
            BookDto bookDto = this.bookService.findBookById(bookId);
            log.info("Book with ID: {} found", bookId);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            log.error("Book with ID: {} not found. Error: {}", bookId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching book with ID: {}. Error: {}", bookId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a book by its ID
    @PutMapping("/book/{bookId}")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto, @PathVariable Integer bookId) {
        log.info("Received request to update book with ID: {}. Updated BookDto: {}", bookId, bookDto);
        try {
            BookDto updatedBook = this.bookService.updateBook(bookDto, bookId);
            log.info("Book with ID: {} updated successfully", bookId);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to update book with ID: {}. Error: {}", bookId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
