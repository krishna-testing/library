package org.clx.library.services;

import lombok.AllArgsConstructor;
import org.clx.library.dto.AuthorDto;
import org.clx.library.dto.AuthorRequest;
import org.clx.library.exception.AuthorNotFoundException;
import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;




    public AuthorRequest createAuthor(AuthorRequest authorRequest) {
        // Convert DTO to entity
//        Author author = mapToEntity(authorDto);
        Author author = authorRequest.authorRequestToAuthor(authorRequest);
        Author savedAuthor = authorRepository.save(author);
        logger.info("Author created successfully with ID: {}", savedAuthor.getId());

        return authorRequest;
    }

    public AuthorDto findAuthorById(Integer authorId) throws AuthorNotFoundException {
        logger.info("Received request to find author with ID: {}", authorId);
        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent()) {
            logger.info("Author with ID: {} found", authorId);
            return mapToDto(author.get());
        } else {
            logger.error("Author with ID: {} not found", authorId);
            throw new AuthorNotFoundException("User does not exist with userId: " + authorId);
        }
    }

    public AuthorDto updateAuthor(AuthorDto authorDto, Integer authorId) throws AuthorNotFoundException {
        logger.info("Received request to update author with ID: {}", authorId);
        Optional<Author> existingAuthor = authorRepository.findById(authorId);
        if (existingAuthor.isEmpty()) {
            logger.error("Author with ID: {} not found. Cannot update.", authorId);
            throw new AuthorNotFoundException("User does not exist with ID: " + authorId);
        }

        Author authorToUpdate = existingAuthor.get();

        // Update fields from DTO if not null
        if (authorDto.getName() != null) {
            authorToUpdate.setName(authorDto.getName());
            logger.info("Author name updated to: {}", authorDto.getName());
        }
        if (authorDto.getEmail() != null) {
            authorToUpdate.setEmail(authorDto.getEmail());
            logger.info("Author email updated to: {}", authorDto.getEmail());
        }

        Author updatedAuthor = authorRepository.save(authorToUpdate);
        logger.info("Author with ID: {} updated successfully", authorId);

        // Return updated AuthorDto
        return mapToDto(updatedAuthor);
    }

    public AuthorDto updateAuthor(AuthorDto authorDto) {
        logger.info("Request is now received to update author: {}", authorDto);
        try {
            Author author = mapToEntity(authorDto);
            authorRepository.updateAuthorDetails(author);
            logger.info("Author details updated successfully for author: {}", authorDto);
            return authorDto; // Assuming the input DTO is sufficient
        } catch (Exception e) {
            logger.error("Error occurred while updating author details: {}", e.getMessage(), e);
            return null;
        }
    }

    public void deleteAuthor(int id) {
        logger.info("Received request to delete author with ID: {}", id);
        try {
            authorRepository.deleteCustom(id);
            logger.info("Author with ID: {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting author with ID: {}: {}", id, e.getMessage(), e);
        }
    }
}
