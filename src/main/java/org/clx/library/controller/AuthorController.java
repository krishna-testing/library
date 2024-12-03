package org.clx.library.controller;

import lombok.RequiredArgsConstructor;
import org.clx.library.dto.AuthorDto;
import org.clx.library.dto.AuthorRequest;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.payload.ApiResponse;
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
    private static final String MESSAGE = "failed";

    @PostMapping("/createAuthor")
    public ResponseEntity<ApiResponse> createAuthor(@RequestBody AuthorRequest authorRequest) {
            // Pass the AuthorRequest to the service layer
            authorService.createAuthor(authorRequest);
            logger.info("Author created successfully.");

            // Create a success response
            ApiResponse response = new ApiResponse(HttpStatus.CREATED,"Author created successfully",null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    @PutMapping("/updateAuthor/{authorId}")
    public ResponseEntity<ApiResponse> updateAuthor(@RequestBody AuthorRequest authorRequest, @PathVariable Integer authorId) {
        logger.info("Received request to update author with ID: {}", authorId);
        try {
            AuthorRequest updatedAuthor = authorService.updateAuthor(authorRequest, authorId);
            logger.info("Author with ID: {} updated successfully", authorId);

            ApiResponse response = new ApiResponse(HttpStatus.ACCEPTED,"Author updated successfully", updatedAuthor);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (ResourceNotFoundException e) {
            logger.error("Author with ID: {} not found", authorId);

            // Create a not-found response
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND,"Author not found", e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Delete Author Endpoint by ID
    @DeleteMapping("/deleteAuthor/{id}")
    public ResponseEntity<ApiResponse> deleteAuthor(@PathVariable int id) {
        logger.info("Received request to delete author with ID: {}", id);
        try {
            // Call service to delete the author by ID
            authorService.deleteAuthor(id);
            logger.info("Author deleted successfully");
            ApiResponse response = new ApiResponse(HttpStatus.OK, "Author successfully deleted!!", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Find Author by ID
    @GetMapping("/findAuthor/{authorId}")
    public ResponseEntity<ApiResponse> findAuthorById(@PathVariable Integer authorId) {
        logger.info("Received request to find author with ID: {}", authorId);
        try {
            // Pass the ID to the service layer and return the AuthorDto
            AuthorDto authorDto = authorService.findAuthorById(authorId);
            logger.info("Author with ID: {} found", authorId);

            // Create a success response
            ApiResponse response = new ApiResponse(HttpStatus.OK,"Author found successfully",authorDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Author with ID: {} not found", authorId);

            // Create a not-found response
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND,MESSAGE, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
