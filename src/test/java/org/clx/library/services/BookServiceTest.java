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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        Book book = new Book(1, "Book Title", null, null, null, true, null);

        // Call the method to test
        bookService.createBook(book);

        // Verify that the save method was called on the mocked repository
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testGetBooksByGenreAndAuthor() {
        String genre = "Science Fiction";
        String author = "Isaac Asimov";
        boolean isAvailable = true;
        Book book = new Book(1, "Foundation", null, null, null, true, null);

        when(bookRepository.findBooksByGenre_Author(genre, author, isAvailable)).thenReturn(Collections.singletonList(book));

        List<Book> books = bookService.getBooks(genre, isAvailable, author);

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Foundation", books.get(0).getName());

        // Verify the repository method was called correctly
        verify(bookRepository, times(1)).findBooksByGenre_Author(genre, author, isAvailable);
    }


    @Test
    void testGetBooksByAvailability() {
        boolean isAvailable = true;
        Book book = new Book(3, "1984", null, null, null, true, null);

        when(bookRepository.findBooksByAvailability(isAvailable)).thenReturn(Collections.singletonList(book));

        List<Book> books = bookService.getBooks(null, isAvailable, null);

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("1984", books.get(0).getName());

        // Verify the repository method was called correctly
        verify(bookRepository, times(1)).findBooksByAvailability(isAvailable);
    }


    @Test
    void testGetBooks_ByAuthor() {
        // Given
        String authorName = "Author Name";
        List<Book> books = new ArrayList<>();
        books.add(new Book(1,"abhay", Genre.POLITICAL_SCIENCE,null,null,true,null));

        // Mocking the repository method for finding books by author and availability
        when(bookRepository.findBooksByAuthor(authorName, true)).thenReturn(books);

        // When: Call the service method
        List<Book> result = bookService.getBooks(null, true, authorName);

        // Then: Assert the result contains the expected book
        assertEquals(1, result.size());
        assertEquals("abhay", result.get(0).getName());

        // Verify that the method in bookRepository was called once with the correct parameters
        verify(bookRepository, times(1)).findBooksByAuthor(authorName, true);
    }

    @Test
    void testGetBooks_ByAvailability() {
        // Given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1,"Available Book", Genre.CHEMISTRY, null,null,true,null));
        when(bookRepository.findBooksByAvailability(true)).thenReturn(books);

        // When
        List<Book> result = bookService.getBooks(null, true, null);

        // Then
        assertEquals(1, result.size());
        assertEquals("Available Book", result.get(0).getName());
        verify(bookRepository, times(1)).findBooksByAvailability(true);
    }
}
