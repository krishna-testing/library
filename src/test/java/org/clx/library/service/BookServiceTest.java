package org.clx.library.services;

import org.clx.library.model.Book;
import org.clx.library.model.Genre;
import org.clx.library.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        // Given
        Book book = new Book("Sample Book", Genre.HISTORY, null);

        // When
        bookService.createBook(book);

        // Then
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testGetBooks_ByGenreAndAuthor() {
        // Given
        String genre = "FICTIONAL";
        String author = "Author Name";
        List<Book> books = new ArrayList<>();
        books.add(new Book("Sample Fictional Book", Genre.FICTIONAL, null));
        when(bookRepository.findBooksByGenre_Author(genre, author, true)).thenReturn(books);

        // When
        List<Book> result = bookService.getBooks(genre, true, author);

        // Then
        assertEquals(1, result.size());
        assertEquals("Sample Fictional Book", result.get(0).getName());
        verify(bookRepository, times(1)).findBooksByGenre_Author(genre, author, true);
    }

    @Test
    void testGetBooks_ByGenre() {
        // Given
        String genre = "HISTORY";
        List<Book> books = new ArrayList<>();
        books.add(new Book("History Book", Genre.HISTORY, null));
        when(bookRepository.findBooksByGenre(genre, true)).thenReturn(books);

        // When
        List<Book> result = bookService.getBooks(genre, true, null);

        // Then
        assertEquals(1, result.size());
        assertEquals("History Book", result.get(0).getName());
        verify(bookRepository, times(1)).findBooksByGenre(genre, true);
    }

    @Test
    void testGetBooks_ByAuthor() {
        // Given
        String author = "Author Name";
        List<Book> books = new ArrayList<>();
        books.add(new Book("Book by Author", Genre.POLITICAL_SCIENCE, null));
        when(bookRepository.findBooksByAuthor(author, true)).thenReturn(books);

        // When
        List<Book> result = bookService.getBooks(null, true, author);

        // Then
        assertEquals(1, result.size());
        assertEquals("Book by Author", result.get(0).getName());
        verify(bookRepository, times(1)).findBooksByAuthor(author, true);
    }

    @Test
    void testGetBooks_ByAvailability() {
        // Given
        List<Book> books = new ArrayList<>();
        books.add(new Book("Available Book", Genre.CHEMISTRY, null));
        when(bookRepository.findBooksByAvailability(true)).thenReturn(books);

        // When
        List<Book> result = bookService.getBooks(null, true, null);

        // Then
        assertEquals(1, result.size());
        assertEquals("Available Book", result.get(0).getName());
        verify(bookRepository, times(1)).findBooksByAvailability(true);
    }
}
