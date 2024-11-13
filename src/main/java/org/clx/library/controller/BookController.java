package org.clx.library.controller;

import org.clx.library.model.Book;
import org.clx.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.clx.library.exception.AuthorException;


@RestController
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
        try {
            Book createdBook = bookService.createBook(book, authorId);
            return new ResponseEntity<>("Book created with ID: " + createdBook.getId(), HttpStatus.CREATED);
        } catch (AuthorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Delete a Book by ID (and ensure it's the author's book)
    @DeleteMapping("/deleteBook")
    public ResponseEntity<String> deleteBook(@RequestParam Integer bookId, @RequestParam Integer authorId) {
        try {
            String message = bookService.deleteBook(bookId, authorId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get all books
    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) String author) {
        List<Book> books = bookService.getBooks(genre, isAvailable != null ? isAvailable : true, author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Get book by ID
    @GetMapping("/getBook/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        try {
            Book book = bookService.findBookById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Save/Remove a book from an author's saved books
    @PutMapping("/saveBook/{bookId}")
    public ResponseEntity<Book> saveBook(@PathVariable Integer bookId, @RequestParam Integer authorId) {
        try {
            Book updatedBook = bookService.savedBook(bookId, authorId);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
