package org.clx.library.services;

import org.clx.library.dto.BookDto;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.exception.UnauthorizedBookDeletionException;
import org.clx.library.model.Author;
import org.clx.library.model.Book;
import org.clx.library.model.Genre;
import org.clx.library.repositories.BookRepository;
import org.clx.library.repositories.AuthorRepository;
import org.clx.library.dto.AuthorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ModelMapper modelMapper; //
    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    private BookDto bookDto;
    private Book book;
    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bookDto = new BookDto();
        bookDto.setName("Book 1");
        bookDto.setGenre(Genre.FICTIONAL);
        bookDto.setAvailable(true);

        book = new Book();
        book.setId(1);
        book.setName("Book 1");
        book.setGenre(Genre.FICTIONAL);
        book.setAvailable(true);

        authorDto = new AuthorDto();
        authorDto.setId(1);
        authorDto.setName("John Doe");
    }

    @Test
    void testCreateBook_AuthorNotFound() {
        // Arrange
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.createBook(bookDto, 1));
    }

    @Test
    void testDeleteBook_Success() {
        // Arrange
        Book bookToDelete = new Book();
        bookToDelete.setId(1);
        bookToDelete.setName("Book 1");

        // Create and set the Author object for the book
        Author author = new Author();
        author.setId(1); // Set the author ID
        bookToDelete.setAuthor(author); // Set the author for the book

        // Mock the repository and service methods
        when(bookRepository.findById(1)).thenReturn(Optional.of(bookToDelete));
        when(authorService.findAuthorById(1)).thenReturn(authorDto);

        // Act
        String result = bookService.deleteBook(1, 1);

        // Assert
        assertEquals("Book deleted successfully", result);
        verify(bookRepository, times(1)).delete(bookToDelete);
    }

    @Test
    void testDeleteBook_Unauthorized() {
        // Arrange
        // Create a book and set its author with a specific ID (different from the authorId passed to deleteBook)
        Book bookToDelete = new Book();
        bookToDelete.setId(1);
        bookToDelete.setName("Book 1");

        Author author = new Author();
        author.setId(2);  // Author ID is 2, which should not match the authorId 1 passed in the test
        bookToDelete.setAuthor(author);  // Assign the author to the book

        // Mock the bookRepository to return the book when findById is called
        when(bookRepository.findById(1)).thenReturn(Optional.of(bookToDelete));

        // Mock the authorService to return an AuthorDto with ID 1 (doesn't match the book's author ID)
        AuthorDto authorDto1 = new AuthorDto();
        authorDto1.setId(1);  // Different ID than the book's author
        when(authorService.findAuthorById(1)).thenReturn(authorDto);

        // Act & Assert
        assertThrows(UnauthorizedBookDeletionException.class, () -> bookService.deleteBook(1, 1));
    }


    @Test
    void testFindBookById_Success() {
        Book books = new Book();
        books.setId(1);
        books.setName("Book 1");

        BookDto bookDtos = new BookDto();
        bookDtos.setId(1);
        bookDtos.setName("Book 1");

        when(bookRepository.findById(1)).thenReturn(Optional.of(books));

        when(modelMapper.map(books, BookDto.class)).thenReturn(bookDtos);

        BookDto foundBook = bookService.findBookById(1);

        assertNotNull(foundBook);
        assertEquals(1, foundBook.getId());   // Ensure the id is correctly set
        assertEquals("Book 1", foundBook.getName());   // Ensure the name is correctly mapped
    }

    @Test
    void testFindBookById_NotFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.findBookById(1));
    }

    @Test
    void testUpdateBook_Success() {
        // Arrange
        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setName("Updated Book");
        updatedBookDto.setGenre(Genre.FICTIONAL);
        updatedBookDto.setAvailable(true);

        Book books = new Book();
        books.setId(1);
        books.setName("Updated Book");
        books.setGenre(Genre.FICTIONAL);
        books.setAvailable(true);

        ModelMapper mockModelMapper = mock(ModelMapper.class);
        when(mockModelMapper.map(any(Book.class), eq(BookDto.class))).thenReturn(updatedBookDto);

        BookService bookServiceWithMockedModelMapper = new BookService(authorRepository, bookRepository, authorService, mockModelMapper );

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto updatedBook = bookServiceWithMockedModelMapper.updateBook(updatedBookDto, 1);

        assertNotNull(updatedBook);
        assertEquals("Updated Book", updatedBook.getName());
        assertEquals(Genre.FICTIONAL, updatedBook.getGenre());
    }


    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(bookDto, 1));
    }

    @Test
    void testGetBooks_ByGenreAndAuthor_Success() {
        when(bookRepository.findBooksByGenre_Author(Genre.FICTIONAL, "John Doe", true)).thenReturn(List.of(book));

        List<Book> books = bookService.getBooks("FICTIONAL", true, "John Doe");

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Book 1", books.get(0).getName());
    }
}
