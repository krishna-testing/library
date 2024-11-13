package org.clx.library.controller;

import org.clx.library.exception.AuthorException;
import org.clx.library.model.Author;
import org.clx.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorController {

    @Autowired
    AuthorService authorService;


    @PostMapping("/createAuthor")
    public ResponseEntity<String> createAuthor( @RequestBody Author author) {
        // Call the service to create the author
        Author createdAuthor = authorService.createAuthor(author);

        // Return a response with a success message and the ID of the created Author
        return new ResponseEntity<>("Author created with ID: ", HttpStatus.CREATED);
    }

    @PutMapping("/updateAuthor/{authorId}")
    public ResponseEntity<String> updateAuthor(@RequestBody Author author, @PathVariable Integer authorId) {
        try {
            authorService.updateAuthor(author, authorId);
            return new ResponseEntity<>("Author updated!!", HttpStatus.ACCEPTED);
        } catch (AuthorException e) {
            // Handle the exception (e.g., author not found)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable int id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>("Author deleted!!", HttpStatus.NO_CONTENT);
    }

}

