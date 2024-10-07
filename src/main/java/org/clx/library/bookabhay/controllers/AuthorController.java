//package org.clx.library.bookabhay.controllers;
//
//import lombok.RequiredArgsConstructor;
//import org.clx.library.bookabhay.entities.Author;
//import org.clx.library.bookabhay.service.AuthorService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/authors")
//@RequiredArgsConstructor
//public class AuthorController {
//    private final AuthorService authorService;
//
//
//    @PostMapping
//    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
//        Author createdAuthor = authorService.saveAuthor(author);
//        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Author> getAuthorById(@PathVariable Integer id) {
//        Author author = authorService.getAuthorById(id);
//        return author != null ? new ResponseEntity<>(author, HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Author>> getAllAuthors() {
//        List<Author> authors = authorService.getAllAuthors();
//        return new ResponseEntity<>(authors, HttpStatus.OK);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Author> updateAuthor(@PathVariable Integer id, @RequestBody Author author) {
//        author.setId(id);
//        Author updatedAuthor = authorService.updateAuthor(author);
//        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAuthor(@PathVariable Integer id) {
//        authorService.deleteAuthor(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//}
//
//
