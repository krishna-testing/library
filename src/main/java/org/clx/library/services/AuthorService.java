package org.clx.library.services;

import lombok.AllArgsConstructor;
import org.clx.library.dto.AuthorDto;
import org.clx.library.dto.AuthorRequest;
import org.clx.library.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private final WebClient webClient;

    public AuthorRequest createAuthor(AuthorRequest authorRequest) {
        logger.info("Sending request to create author");
        return webClient.post()
                .uri("/createAuthor")
                .body(Mono.just(authorRequest), AuthorRequest.class)
                .retrieve()
                .bodyToMono(AuthorRequest.class)
                .doOnSuccess(response -> logger.info("Author created successfully"))
                .block();
    }

    public AuthorDto findAuthorById(Integer authorId) throws ResourceNotFoundException {
        logger.info("Fetching author with ID: {}", authorId);
        return webClient.get()
                .uri("/findAuthor/{authorId}", authorId)
                .retrieve()
                .bodyToMono(AuthorDto.class)
                .doOnError(e -> logger.error("Error fetching author: {}", e.getMessage()))
                .block();
    }

    public AuthorRequest updateAuthor(AuthorRequest authorRequest, Integer authorId) throws ResourceNotFoundException {
        logger.info("Sending request to update author with ID: {}", authorId);
        return webClient.put()
                .uri("/updateAuthor/{authorId}", authorId)
                .body(Mono.just(authorRequest), AuthorRequest.class)
                .retrieve()
                .bodyToMono(AuthorRequest.class)
                .doOnSuccess(response -> logger.info("Author updated successfully"))
                .block();
    }

    public void deleteAuthor(int id) {
        logger.info("Sending request to delete author with ID: {}", id);
        webClient.delete()
                .uri("/deleteAuthor/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> logger.info("Author deleted successfully"))
                .block();
    }
}
