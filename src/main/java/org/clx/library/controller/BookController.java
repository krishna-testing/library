package org.clx.library.controller;

import lombok.extern.slf4j.Slf4j;
import org.clx.library.dto.BookDto;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.model.Book;
import org.clx.library.payload.ApiResponse;
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
    private static final String MESSAGE = "failed";

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/authors/{authorId}/books")
    public ResponseEntity<ApiResponse> createBook(@RequestBody BookDto bookDto, @PathVariable int authorId) {
        log.info("Received request to create book with authorId: {}, BookDto: {}", authorId, bookDto);

        try {
            BookDto createdBook = this.bookService.createBook(bookDto, authorId);
            log.info("Book created successfully with ID: {}", createdBook.getId());
            ApiResponse response = new ApiResponse(HttpStatus.CREATED, "Book created successfully", null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create book with authorId: {}, BookDto: {}. Error: {}", authorId, bookDto, e.getMessage());
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteBook")
    public ResponseEntity<ApiResponse> deleteBook(@RequestParam Integer bookId, @RequestParam Integer authorId) {
        log.info("Received request to delete book with ID: {} for authorId: {}", bookId, authorId);
        try {
            String message = bookService.deleteBook(bookId, authorId);
            log.info("Book with ID: {} deleted successfully", bookId);
            ApiResponse response = new ApiResponse(HttpStatus.OK, "Book deleted successfully", message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete book with ID: {}. Error: {}", bookId, e.getMessage());
            ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST, MESSAGE, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getBooks")
    public ResponseEntity<ApiResponse> getBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false, defaultValue = "true") Boolean isAvailable,
            @RequestParam(required = false) String author) {
        log.info("Received request to get books with parameters - Genre: {}, Available: {}, Author: {}", genre, isAvailable, author);

        try {
            List<BookDto> books = bookService.getBooks(genre, isAvailable, author);
            log.info("Returning {} books", books.size());
            ApiResponse response = new ApiResponse(HttpStatus.OK, "Books retrieved successfully", books);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to retrieve books. Error: {}", e.getMessage());
            ApiResponse response = new ApiResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/posts/{bookId}")
    public ResponseEntity<ApiResponse> getBookById(@PathVariable Integer bookId) {
        log.info("Received request to get book with ID: {}", bookId);
        try {
            BookDto bookDto = this.bookService.findBookById(bookId);
            log.info("Book with ID: {} found", bookId);
            ApiResponse response = new ApiResponse(HttpStatus.OK, "Book retrieved successfully", bookDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            log.error("Book with ID: {} not found. Error: {}", bookId, e.getMessage());
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, "Book not found", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse> updateBook(@RequestBody BookDto bookDto, @PathVariable Integer bookId) {
        log.info("Received request to update book with ID: {}. Updated BookDto: {}", bookId, bookDto);
        try {
            BookDto updatedBook = this.bookService.updateBook(bookDto, bookId);
            log.info("Book with ID: {} updated successfully", bookId);
            ApiResponse response = new ApiResponse(HttpStatus.OK, "Book updated successfully", updatedBook);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            log.error("Book with ID: {} not found. Error: {}", bookId, e.getMessage());
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, "Book not found", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
