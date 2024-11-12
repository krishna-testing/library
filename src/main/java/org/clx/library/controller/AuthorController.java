package org.clx.library.controller;

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
    public ResponseEntity<String> createAuthor(@RequestBody Author author){
        authorService.createAuthor(author);
        return new ResponseEntity<>("Author created", HttpStatus.CREATED);
    }
    @PutMapping("/updateAuthor")
    public ResponseEntity<String> updateAuthor(@RequestBody Author author){
        authorService.updateAuthor(author);
        return new ResponseEntity<>("Author updated!!",HttpStatus.ACCEPTED);

    }

    @DeleteMapping("/deleteAuthor")
    public ResponseEntity<String> deleteAuthor(@RequestParam("id") int id){
        authorService.deleteAuthor(id);
        return new ResponseEntity<>("Author deleted!!",HttpStatus.ACCEPTED);

    }

}
