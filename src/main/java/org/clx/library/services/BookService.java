package org.clx.library.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.dto.AuthorDto;
import org.clx.library.dto.BookDto;
import org.clx.library.payload.BookResponse;

import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.exception.UnauthorizedBookDeletionException;
import org.clx.library.model.Author;
import org.clx.library.model.Book;
import org.clx.library.model.Genre;
import org.clx.library.repositories.AuthorRepository;
import org.clx.library.repositories.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Service
@Slf4j
@AllArgsConstructor
public class BookService {
    private final WebClient webClient;

    public BookDto createBook(BookDto bookDto, int authorId) {
        bookDto.setId(authorId);

        return webClient.post()
                .uri("/api/authors/{authorId}/books",authorId)
                .bodyValue(bookDto)
                .retrieve()
                .bodyToMono(BookDto.class)
                .block();
    }

    public String deleteBook(Integer bookId, Integer authorId) {
        try {
            webClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/deleteBook")
                            .queryParam("bookId", bookId)
                            .queryParam("authorId", authorId)
                            .build())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            log.info("Book deleted successfully with ID: {}", bookId);
            return "Book deleted successfully";
        } catch (WebClientResponseException e) {
            log.error("Error deleting book: {}", e.getMessage());
            throw new ResourceNotFoundException("Book", "ID", bookId);
        }
    }

    public BookDto findBookById(Integer bookId) {
        return webClient.get()
                .uri("/api/posts/{bookId}", bookId)
                .retrieve()
                .bodyToMono(BookDto.class)
                .block();
    }

    public BookDto updateBook(BookDto bookDto, int bookId) {
        return webClient.put()
                .uri("/api/book/{bookId}", bookId)
                .bodyValue(bookDto)
                .retrieve()
                .bodyToMono(BookDto.class)
                .block();
    }


    public List<BookDto> getBooks(String genre, boolean isAvailable, String author) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/getBooks")
                        .queryParam("genre", genre)
                        .queryParam("available", isAvailable)
                        .queryParam("author", author)
                        .build())
                .retrieve()
                .bodyToFlux(BookDto.class)
                .collectList()
                .block();
    }

}
