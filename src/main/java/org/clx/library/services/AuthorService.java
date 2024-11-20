package org.clx.library.services;

import lombok.AllArgsConstructor;
import org.clx.library.controller.AuthorController;
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

    public Author createAuthor(Author author) {
        try {
            Author newAuthor = new Author();
            newAuthor.setId(author.getId());
            newAuthor.setName(author.getName());
            newAuthor.setEmail(author.getEmail());
            newAuthor.setAge(author.getAge());
            newAuthor.setCountry(author.getCountry());
            newAuthor.setBooksWritten(author.getBooksWritten());

            Author savedAuthor = authorRepository.save(newAuthor);
            logger.info("Author created successfully with ID: {}", savedAuthor.getId());
            return savedAuthor;
        } catch (Exception ae) {
            logger.error("Error occurred while creating author: {}", ae.getMessage(), ae);
            throw ae;
        }
    }
    public Author findAuthorById(Integer authorId) throws AuthorNotFoundException {
        logger.info("Received request to find author with ID: {}", authorId);
        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent()) {
            logger.info("Author with ID: {} found", authorId);
            return author.get();
        } else {
            logger.error("Author with ID: {} not found", authorId);
            throw new AuthorNotFoundException("User does not exist with userId: " + authorId);
        }
    }

    public Author updateAuthor(Author author, Integer authorId) throws AuthorNotFoundException {
        logger.info("Received request to update author with ID: {}", authorId);
        Optional<Author> existingAuthor = authorRepository.findById(authorId);
        if (existingAuthor.isEmpty()) {
            logger.error("Author with ID: {} not found. Cannot update.", authorId);
            throw new AuthorNotFoundException("User does not exist with ID: " + authorId);
        }

        Author oldAuthor = existingAuthor.get();
        if (author.getName() != null) {
            oldAuthor.setName(author.getName());
            logger.info("Author name updated to: {}", author.getName());
        }
        if (author.getEmail() != null) {
            oldAuthor.setEmail(author.getEmail());
            logger.info("Author email updated to: {}", author.getEmail());
        }

        Author updatedAuthor = authorRepository.save(oldAuthor);
        logger.info("Author with ID: {} updated successfully", authorId);
        return updatedAuthor;
    }
    public void updateAuthor(Author author) {
        logger.info("Request is now received ti update authorRequest for author:{}",author);
        try {
            authorRepository.updateAuthorDetails(author);
            logger.info("Author details updated successfully for author: {}", author);
        } catch (Exception e) {
            logger.error("Error occurred while updating author details: {}", e.getMessage(), e);
        }    }

    public void deleteAuthor(int id) {
        logger.info("Received request to delete author with ID: {}", id);
        try {
            authorRepository.deleteCustom(id);
            logger.info("Author with ID: {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting author with ID: {}: {}", id, e.getMessage(), e);
        }    }
}