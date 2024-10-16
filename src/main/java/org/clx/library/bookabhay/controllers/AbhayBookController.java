//package org.clx.library.bookabhay.controllers;
//
//
//
//import lombok.AllArgsConstructor;
////import org.clx.library.bookabhay.entities.Book;
////import org.clx.library.bookabhay.service.AbhayBookService;
//import org.clx.library.user.model.User;
//import org.clx.library.user.response.ApiResponse;
//import org.clx.library.user.service.UserService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/books")
//@AllArgsConstructor
//public class AbhayBookController {
////    private final AbhayBookService bookService;
//    private final UserService userService;
//
//    @PostMapping
//    public ResponseEntity<Book> createBook(@RequestHeader("Authorization") String jwt, @RequestBody Book book) throws Exception {
//        User reqUser = userService.findUserByJwt(jwt);
//        Book createdBook = bookService.createBook(book, reqUser.getId());
//        return new ResponseEntity<>(createdBook, HttpStatus.ACCEPTED);
//    }
//
//    @DeleteMapping("/{bookId}")
//    public ResponseEntity<ApiResponse> deleteBook(@RequestHeader("Authorization") String jwt, @PathVariable Integer bookId) throws Exception {
//        User reqUser = userService.findUserByJwt(jwt);
//        String message = bookService.deleteBook(bookId, reqUser.getId());
//        ApiResponse res = new ApiResponse(message, true);
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }
//
//    @GetMapping("/{bookId}")
//    public ResponseEntity<Book> findBookById(@PathVariable Integer bookId) throws Exception {
//        Book book = bookService.findBookById(bookId);
//        return new ResponseEntity<>(book, HttpStatus.OK);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Book>> findUsersBooks(@PathVariable Integer userId) {
//        List<Book> books = bookService.findBookByUserId(userId);
//        return new ResponseEntity<>(books, HttpStatus.OK);
//    }
//
//    @GetMapping()
//    public ResponseEntity<List<Book>> findAllBooks() {
//        List<Book> books = bookService.getAllBooks();
//        return new ResponseEntity<>(books, HttpStatus.OK);
//    }
//
//
//}
