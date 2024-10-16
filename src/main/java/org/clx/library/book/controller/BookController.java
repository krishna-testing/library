package org.clx.library.book.controller;

import jakarta.validation.Valid;
import org.clx.library.book.Service.BookService;
import org.clx.library.book.entity.Book;
import org.clx.library.book.Exception.ApiResponse; // Import ApiResponse
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks(){
        List<Book> allBooks = bookService.getAllBooks();
        ApiResponse<List<Book>> response = new ApiResponse<>(true, allBooks, "Books retrieved successfully.", HttpStatus.OK,null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable Long id){
        try {
            Book book = bookService.getBookById(id);
            ApiResponse<Book> response = new ApiResponse<>(true, book, "Book retrieved successfully.", HttpStatus.OK,null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Book> response = new ApiResponse<>(false, null, "Book not found.", HttpStatus.NOT_FOUND,null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<ApiResponse<List<Book>>> searchBooksByTitle(@RequestParam String title){
        List<Book> books = bookService.searchBooksByTitle(title);
        ApiResponse<List<Book>> response;

        if (books.isEmpty()) {
            response = new ApiResponse<>(false, null, "No books found with the specified title.", HttpStatus.NO_CONTENT,null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response = new ApiResponse<>(true, books, "Books retrieved successfully.", HttpStatus.OK,null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/search/author")
    public ResponseEntity<ApiResponse<List<Book>>> searchBooksByAuthor(@RequestParam String author) {
        List<Book> books = bookService.searchBooksByAuthor(author);
        ApiResponse<List<Book>> response;

        if (books.isEmpty()) {
            response = new ApiResponse<>(false, null, "No books found by the specified author.", HttpStatus.NO_CONTENT,null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response = new ApiResponse<>(true, books, "Books retrieved successfully.", HttpStatus.OK,null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Book>> addBook(@Valid @RequestBody Book book){
        Book savedBook = bookService.saveBook(book);
        ApiResponse<Book> response = new ApiResponse<>(true, savedBook, "Book added successfully.", HttpStatus.CREATED,null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails){
        try {
            Book existingBook = bookService.getBookById(id);
            existingBook.setTitle(bookDetails.getTitle());
            existingBook.setAuthor(bookDetails.getAuthor());
            existingBook.setGenre(bookDetails.getGenre());
            existingBook.setPublisher(bookDetails.getPublisher());
            existingBook.setIsbn(bookDetails.getIsbn());
            existingBook.setPublisherYear(bookDetails.getPublisherYear());
            Book updatedBook = bookService.saveBook(existingBook);
            ApiResponse<Book> response = new ApiResponse<>(true, updatedBook, "Book updated successfully.", HttpStatus.OK,null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Book> response = new ApiResponse<>(false, null, "Book not found.", HttpStatus.NOT_FOUND,null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBookById(id);
            ApiResponse<Void> response = new ApiResponse<>(true, null, "Book deleted successfully.", HttpStatus.NO_CONTENT,null);
            return ResponseEntity.status(HttpStatus.OK).body(response); // Return 204 No Content after deletion
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, null, "Book not found.", HttpStatus.NOT_FOUND,null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Return 404 Not Found if book doesn't exist
        }
    }
}
