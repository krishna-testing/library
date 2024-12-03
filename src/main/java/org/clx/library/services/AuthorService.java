package org.clx.library.services;

import lombok.AllArgsConstructor;
import org.clx.library.dto.AuthorDto;
import org.clx.library.dto.AuthorRequest;
import org.clx.library.exception.ResourceNotFoundException;
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

        Author author = authorRequest.authorRequestToAuthor();
        Author savedAuthor = authorRepository.save(author);
        logger.info("Author created successfully with ID: {}", savedAuthor.getId());

        return authorRequest;
    }

    public AuthorDto findAuthorById(Integer authorId) throws ResourceNotFoundException {
        logger.info("Received request to find author with ID: {}", authorId);
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("author", "id", authorId));
        AuthorDto authorDto = new AuthorDto();
        return authorDto.mapToDto(author);
    }

    public AuthorRequest updateAuthor(AuthorRequest authorRequest, Integer authorId) throws ResourceNotFoundException {
        logger.info("Received request to update author with ID: {}", authorId);
        Optional<Author> existingAuthor = authorRepository.findById(authorId);
        if (existingAuthor.isEmpty()) {
            logger.error("Author with ID: {} not found. Cannot update.", authorId);
            throw new ResourceNotFoundException("Author", "id", authorId);
        }

        Author authorToUpdate = existingAuthor.get();

        // Update fields from DTO if not null
        if (authorRequest.getName() != null) {
            authorToUpdate.setName(authorRequest.getName());
            logger.info("Author name updated to: {}", authorRequest.getName());
        }
        if (authorRequest.getEmail() != null) {
            authorToUpdate.setEmail(authorRequest.getEmail());
            logger.info("Author email updated to: {}", authorRequest.getEmail());
        }
        if (authorRequest.getCountry() != null) {
            authorToUpdate.setCountry(authorRequest.getCountry());
            logger.info("Author country updated to: {}", authorRequest.getCountry());
        }


        Author updatedAuthor = authorRepository.save(authorToUpdate);
        logger.info("Author with ID: {} updated successfully", authorId);

        // Return updated AuthorDto
        return authorRequest.authorToAuthorRequest(updatedAuthor);
    }



    public void deleteAuthor(int id) {
        logger.info("Received request to delete author with ID: {}", id);
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("author", "id", id));
        authorRepository.deleteById(id);
        logger.info("Author with ID: {} deleted successfully", author.getId());
    }

}


