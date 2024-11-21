//package org.clx.library.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.clx.library.exception.AuthorNotFoundException;
//import org.clx.library.model.Author;
//import org.clx.library.services.AuthorService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//public class AuthorController {
//
//    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
//    private final AuthorService authorService;
//
//    @PostMapping("/createAuthor")
//    public ResponseEntity<String> createAuthor(@RequestBody Author author) {
//
//        try {
//            Author createdAuthor = authorService.createAuthor(author);
//            logger.info("Author created successfully with ID: {}", createdAuthor.getId());
//            return new ResponseEntity<>("Author created with ID: " + createdAuthor.getId(), HttpStatus.CREATED);
//        } catch (Exception e) {
//            logger.error("Error creating author: {}", e.getMessage());
//            return new ResponseEntity<>("Failed to create author", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PutMapping("/updateAuthor/{authorId}")
//    public ResponseEntity<String> updateAuthor(@RequestBody Author author, @PathVariable Integer authorId) {
//        logger.info("Received request to update author with ID: {}", authorId);
//        try {
//            authorService.updateAuthor(author, authorId);
//            logger.info("Author with ID: {} updated successfully", authorId);
//            return new ResponseEntity<>("Author updated!!", HttpStatus.ACCEPTED);
//        } catch (AuthorNotFoundException e) {
//            logger.error("Author with ID: {} not found", authorId);
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            logger.error("Error updating author with ID: {}: {}", authorId, e.getMessage());
//            return new ResponseEntity<>("Failed to update author", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteAuthor(@PathVariable int id) {
//        logger.info("Received request to delete author with ID: {}", id);
//        try {
//            authorService.deleteAuthor(id);
//            logger.info("Author with ID: {} deleted successfully", id);
//            return new ResponseEntity<>("Author deleted!!", HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            logger.error("Error deleting author with ID: {}: {}", id, e.getMessage());
//            return new ResponseEntity<>("Failed to delete author", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
package org.clx.library.controller;

import lombok.RequiredArgsConstructor;
import org.clx.library.dto.AuthorDto;
import org.clx.library.exception.AuthorNotFoundException;
import org.clx.library.services.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorService authorService;

    // Create Author Endpoint
    @PostMapping("/createAuthor")
    public ResponseEntity<String> createAuthor(@RequestBody AuthorDto authorDto) {
        try {
            // Pass the AuthorDto to the service layer
            AuthorDto createdAuthor = authorService.createAuthor(authorDto);
            logger.info("Author created successfully with ID: {}", createdAuthor.getId());
            return new ResponseEntity<>("Author created with ID: " + createdAuthor.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating author: {}", e.getMessage());
            return new ResponseEntity<>("Failed to create author", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update Author Endpoint by ID
    @PutMapping("/updateAuthor/{authorId}")
    public ResponseEntity<String> updateAuthor(@RequestBody AuthorDto authorDto, @PathVariable Integer authorId) {
        logger.info("Received request to update author with ID: {}", authorId);
        try {
            // Pass the AuthorDto to the service layer along with the ID
            AuthorDto updatedAuthor = authorService.updateAuthor(authorDto, authorId);
            logger.info("Author with ID: {} updated successfully", authorId);
            return new ResponseEntity<>("Author updated!!", HttpStatus.ACCEPTED);
        } catch (AuthorNotFoundException e) {
            logger.error("Author with ID: {} not found", authorId);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating author with ID: {}: {}", authorId, e.getMessage());
            return new ResponseEntity<>("Failed to update author", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Author Endpoint by ID
    @DeleteMapping("/deleteAuthor/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable int id) {
        logger.info("Received request to delete author with ID: {}", id);
        try {
            // Call service to delete the author by ID
            authorService.deleteAuthor(id);
            logger.info("Author with ID: {} deleted successfully", id);
            return new ResponseEntity<>("Author deleted!!", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting author with ID: {}: {}", id, e.getMessage());
            return new ResponseEntity<>("Failed to delete author", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Find Author by ID
    @GetMapping("/findAuthor/{authorId}")
    public ResponseEntity<Object> findAuthorById(@PathVariable Integer authorId) {
        logger.info("Received request to find author with ID: {}", authorId);
        try {
            // Pass the ID to the service layer and return the AuthorDto
            AuthorDto authorDto = authorService.findAuthorById(authorId);
            logger.info("Author with ID: {} found", authorId);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        } catch (AuthorNotFoundException e) {
            logger.error("Author with ID: {} not found", authorId);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error finding author with ID: {}: {}", authorId, e.getMessage());
            return new ResponseEntity<>("Failed to find author", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
