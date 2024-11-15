package org.clx.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clx.library.exception.AuthorNotFoundException;
import org.clx.library.exception.BookNotFoundException;
import org.clx.library.exception.UnauthorizedBookDeletionException;
import org.clx.library.model.Author;
import org.clx.library.model.Book;
import org.clx.library.model.Genre;
import org.clx.library.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

import org.mockito.Mockito;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private Book book;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        // Initialize a sample Book object for testing
        book = new Book();
        book.setId(1);
        book.setName("Test Book");
    }

    @Test
    void testCreateBook_Success() throws Exception {
        // Arrange
        Integer authorId = 1;

        // Create a mock Author
        Author author = new Author();
        author.setId(authorId);
        author.setName("John Doe");

        // Create a Book object
        Book createdBook = new Book();
        createdBook.setName("Test Book");
        createdBook.setGenre(Genre.FICTIONAL);
        createdBook.setAuthor(author);

        // Mock behavior of bookService.createBook
        when(bookService.createBook(any(Book.class), eq(authorId)))
                .thenReturn(createdBook);

        // Act and Assert
        mockMvc.perform(post("/api/createBook")  // Ensure the correct URL is used
                        .param("authorId", String.valueOf(authorId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdBook)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Book created with ID: " + createdBook.getId()));

        // Verify the service method was called once with the correct parameters
        Mockito.verify(bookService, Mockito.times(1)).createBook(any(Book.class), eq(authorId));
    }

    @Test
    void testCreateBook_BadRequest_AuthorException() throws Exception {
        // Arrange
        Integer authorId = 1;
        when(bookService.createBook(any(Book.class), eq(authorId)))
                .thenThrow(new AuthorNotFoundException("Author not found"));

        Book createdBook = new Book();
        createdBook.setName("Test Book");
        createdBook.setGenre(Genre.FICTIONAL);

        // Act and Assert
        mockMvc.perform(post("/api/createBook")
                        .param("authorId", String.valueOf(authorId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdBook)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Author not found"));
    }


    @Test
    void testDeleteBook_Success() throws Exception {
        // Arrange
        Integer bookId = 1;
        Integer authorId = 1;
        when(bookService.deleteBook(bookId, authorId)).thenReturn("Post deleted Successfully");

        // Act and Assert
        mockMvc.perform(delete("/api/deleteBook")
                        .param("bookId", String.valueOf(bookId))
                        .param("authorId", String.valueOf(authorId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Post deleted Successfully"));

        verify(bookService, times(1)).deleteBook(bookId, authorId);
    }

    @Test
    void testDeleteBook_BadRequest_Exception() throws Exception {
        // Arrange
        Integer bookId = 1;
        Integer authorId = 1;
        when(bookService.deleteBook(bookId, authorId)).thenThrow(new UnauthorizedBookDeletionException("Unauthorized"));

        // Act and Assert
        mockMvc.perform(delete("/api/deleteBook")
                        .param("bookId", String.valueOf(bookId))
                        .param("authorId", String.valueOf(authorId)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unauthorized"));

        verify(bookService, times(1)).deleteBook(bookId, authorId);
    }

    @Test
    void testGetBooks_Success() throws Exception {
        // Arrange
        List<Book> books = Arrays.asList(book);
        when(bookService.getBooks(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(books);

        // Act and Assert
        mockMvc.perform(get("/api/getBooks")
                        .param("genre", "Fiction")
                        .param("isAvailable", "true")
                        .param("author", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Book"));

        verify(bookService, times(1)).getBooks(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString());
    }

    @Test
    void testGetBookById_Success() throws Exception {
        // Arrange
        Integer bookId = 1;
        when(bookService.findBookById(bookId)).thenReturn(book);

        // Act and Assert
        mockMvc.perform(get("/api/getBook/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Book"));

        verify(bookService, times(1)).findBookById(bookId);
    }

    @Test
    void testGetBookById_NotFound() throws Exception {
        // Arrange
        Integer bookId = 1;
        when(bookService.findBookById(bookId)).thenThrow(new BookNotFoundException("Book not found"));

        // Act and Assert
        mockMvc.perform(get("/api/getBook/{id}", bookId))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findBookById(bookId);
    }

    @Test
    void testSaveBook_Success() throws Exception {
        // Arrange
        Integer bookId = 1;
        Integer authorId = 1;
        when(bookService.savedBook(bookId, authorId)).thenReturn(book);

        // Act and Assert
        mockMvc.perform(put("/api/saveBook/{bookId}", bookId)
                        .param("authorId", String.valueOf(authorId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Book"));

        verify(bookService, times(1)).savedBook(bookId, authorId);
    }

    @Test
    void testSaveBook_BadRequest_Exception() throws Exception {
        // Arrange
        Integer bookId = 1;
        Integer authorId = 1;
        when(bookService.savedBook(bookId, authorId)).thenThrow(new BookNotFoundException("Invalid operation"));

        // Act and Assert
        mockMvc.perform(put("/api/saveBook/{bookId}", bookId)
                        .param("authorId", String.valueOf(authorId)))
                .andExpect(status().isBadRequest());

        verify(bookService, times(1)).savedBook(bookId, authorId);
    }
}
