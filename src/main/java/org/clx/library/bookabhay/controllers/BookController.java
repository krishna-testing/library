package org.clx.library.bookabhay.controllers;

import lombok.AllArgsConstructor;
import org.clx.library.bookabhay.entities.Book;
import org.clx.library.bookabhay.service.BookService;
import org.clx.library.user.model.User;
import org.clx.library.user.response.ApiResponse;
import org.clx.library.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;
    @PostMapping
    public ResponseEntity<Book> createPost(@RequestHeader("Authorization") String jwt, @RequestBody Book book) throws Exception {
        User reqUser = userService.findUserByJwt(jwt);
        Book createdBook = bookService.createBook(book, reqUser.getId());
        return new ResponseEntity<>(createdBook, HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{bookId}")
    public ResponseEntity<ApiResponse> deletePost(@RequestHeader("Authorization") String jwt, @PathVariable Integer bookId) throws Exception {
        User reqUser = userService.findUserByJwt(jwt);
        String message = bookService.deleteBook(bookId, reqUser.getId());
        ApiResponse res = new ApiResponse(message, true);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.OK);
    }
    @GetMapping("/{bookId}")
    public ResponseEntity<Book> findBookByIdHandler(@PathVariable Integer bookId) throws Exception {
        Book book = bookService.findBookById(bookId);
        return new ResponseEntity<>(book, HttpStatus.ACCEPTED);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Book>> findUsersBook(@PathVariable Integer userId) {
        List<Book> books = bookService.findBookByUserId(userId);
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<Book>> findAllBook() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }
    @PutMapping("/save/{bookId}")
    public ResponseEntity<Book> savedBookIdHandler(@RequestHeader("Authorization") String jwt, @PathVariable Integer bookId) throws Exception {
        User reqUser = userService.findUserByJwt(jwt);

        Book book = bookService.savedBook(bookId, reqUser.getId());
        return new ResponseEntity<Book>(book, HttpStatus.ACCEPTED);
    }

}
