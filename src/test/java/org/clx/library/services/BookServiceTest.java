package org.clx.library.services;

import org.clx.library.model.Book;
import org.clx.library.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.clx.library.exception.AuthorException;
import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;

import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Author and Book objects
        author = new Author();
        author.setId(1);
        author.setName("Author Name");

        book = new Book();
        book.setId(1);
        book.setName("Book Title");
        book.setAuthor(author);
        book.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateBook() throws AuthorException {
        // Arrange
        Integer authorId = 1;
        when(authorService.findAuthorById(authorId)).thenReturn(author);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        // Act
        Book createdBook = bookService.createBook(book, authorId);

        // Assert
        assertNotNull(createdBook);
        assertEquals(book.getName(), createdBook.getName());
        assertEquals(book.getAuthor().getId(), createdBook.getAuthor().getId());
        verify(bookRepository, times(1)).save(Mockito.any(Book.class));
    }

    @Test
    void testDeleteBook_Success() throws Exception {
        // Arrange
        Integer bookId = 1;
        Integer authorId = 1;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorService.findAuthorById(authorId)).thenReturn(author);

        // Act
        String result = bookService.deleteBook(bookId, authorId);

        // Assert
        assertEquals("Post deleted Successfully", result);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testDeleteBook_Failure() throws Exception {
        // Arrange
        Integer bookId = 1;
        Integer authorId = 1;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorService.findAuthorById(authorId)).thenReturn(new Author()); // Different author

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            bookService.deleteBook(bookId, authorId);
        });

        assertEquals("You can't delete Unother book", exception.getMessage());
    }

    @Test
    void testFindBookById_Success() {
        // Arrange
        Integer bookId = 1;
        Book book1 = new Book();
        book1.setId(bookId);  // Ensure the book has the correct ID

        // Mock the repository to return the book when the findById method is called
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        Book foundBook = bookService.findBookById(bookId);

        // Assert
        assertNotNull(foundBook, "The found book should not be null");
        assertEquals(bookId, foundBook.getId(), "The book ID should match the expected ID");
    }


    @Test
    void testFindBookById_NotFound() {
        // Arrange
        Integer bookId = 1;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            bookService.findBookById(bookId);
        });

        assertEquals("Post not Found with this id1", exception.getMessage());
    }

    @Test
    void testSavedBook() throws Exception {
        // Arrange
        Integer bookId = 1;
        Integer authorId = 1;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorService.findAuthorById(authorId)).thenReturn(author);

        // Act
        Book savedBook = bookService.savedBook(bookId, authorId);

        // Assert
        assertNotNull(savedBook);
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void testGetBooksByGenreAndAuthor() {
        // Arrange
        String genre = "Fiction";
        String authorName = "Author Name";
        boolean isAvailable = true;
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findBooksByGenre_Author(genre, authorName, isAvailable)).thenReturn(books);

        // Act
        List<Book> result = bookService.getBooks(genre, isAvailable, authorName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getName(), result.get(0).getName());
    }

    @Test
    void testGetBooksByGenre() {
        // Arrange
        String genre = "Fiction";
        boolean isAvailable = true;
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findBooksByGenre(genre, isAvailable)).thenReturn(books);

        // Act
        List<Book> result = bookService.getBooks(genre, isAvailable, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getName(), result.get(0).getName());
    }

    @Test
    void testGetBooksByAuthor() {
        // Arrange
        String authorName = "Author Name";
        boolean isAvailable = true;
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findBooksByAuthor(authorName, isAvailable)).thenReturn(books);

        // Act
        List<Book> result = bookService.getBooks(null, isAvailable, authorName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getName(), result.get(0).getName());
    }
}
