package org.clx.library.controller;

import org.clx.library.model.Book;
import org.clx.library.model.Genre;
import org.clx.library.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        // Given
        Book book = new Book(8,"Sample Book", Genre.FICTIONAL, null,null,true,null);

        // When
        ResponseEntity<String> response = bookController.createBook(book,null);

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Book added to the library system", response.getBody());
        verify(bookService, times(1)).createBook(book);
    }

    @Test
    void testGetBooks_NoFilters() {
        // Given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1,"Book1", Genre.HISTORY, null,null,true,null));
        books.add(new Book(1,"Book1",  Genre.POLITICAL_SCIENCE, null,null,true,null));
        when(bookService.getBooks(null, false, null)).thenReturn(books);

        // When
        ResponseEntity<List<Book>> response = bookController.getBooks(null, false, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(bookService, times(1)).getBooks(null, false, null);
    }

    @Test
    void testGetBooks_WithGenre() {
        // Given
        String genre = "FICTIONAL";
        List<Book> books = new ArrayList<>();
        books.add(new Book(2,"Fictional Book", Genre.FICTIONAL, null,null,true,null));
        when(bookService.getBooks(genre, false, null)).thenReturn(books);

        // When
        ResponseEntity<List<Book>> response = bookController.getBooks(genre, false, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Fictional Book", response.getBody().get(0).getName());
        verify(bookService, times(1)).getBooks(genre, false, null);
    }

    @Test
    void testGetBooks_WithAvailability() {
        // Given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1,"Available Book", Genre.CHEMISTRY, null,null,true,null));
        when(bookService.getBooks(null, true, null)).thenReturn(books);

        // When
        ResponseEntity<List<Book>> response = bookController.getBooks(null, true, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Available Book", response.getBody().get(0).getName());
        verify(bookService, times(1)).getBooks(null, true, null);
    }

    @Test
    void testGetBooks_WithGenreAndAuthor() {
        // Given
        String genre = "FICTIONAL";
        String author = "Author Name";
        List<Book> books = new ArrayList<>();
        books.add(new Book(5,"Fictional Book by Author", Genre.FICTIONAL, null, null, true,null));
        when(bookService.getBooks(genre, false, author)).thenReturn(books);

        // When
        ResponseEntity<List<Book>> response = bookController.getBooks(genre, false, author);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Fictional Book by Author", response.getBody().get(0).getName());
        verify(bookService, times(1)).getBooks(genre, false, author);
    }
}
