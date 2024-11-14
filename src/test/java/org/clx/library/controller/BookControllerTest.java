package org.clx.library.controller;

import org.clx.library.model.Book;
import org.clx.library.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

import org.clx.library.exception.AuthorException;
import org.mockito.Mockito;



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
        when(bookService.createBook(Mockito.any(Book.class), Mockito.eq(authorId))).thenReturn(book);

        // Act and Assert
        mockMvc.perform(post("/api/createBook")
                        .param("authorId", String.valueOf(authorId))
                        .contentType("application/json")
                        .content("{ \"name\": \"Test Book\", \"genre\": \"Fiction\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Book created with ID: 1"));

        verify(bookService, times(1)).createBook(Mockito.any(Book.class), Mockito.eq(authorId));
    }

    @Test
    void testCreateBook_BadRequest_AuthorException() throws Exception {
        // Arrange
        Integer authorId = 1;
        when(bookService.createBook(Mockito.any(Book.class), Mockito.eq(authorId))).thenThrow(new AuthorException("Author not found"));

        // Act and Assert
        mockMvc.perform(post("/api/createBook")
                        .param("authorId", String.valueOf(authorId))
                        .contentType("application/json")
                        .content("{ \"name\": \"Test Book\", \"genre\": \"Fiction\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Author not found"));

        verify(bookService, times(1)).createBook(Mockito.any(Book.class), Mockito.eq(authorId));
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
        when(bookService.deleteBook(bookId, authorId)).thenThrow(new Exception("Unauthorized"));

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
        when(bookService.findBookById(bookId)).thenThrow(new Exception("Book not found"));

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
        when(bookService.savedBook(bookId, authorId)).thenThrow(new Exception("Invalid operation"));

        // Act and Assert
        mockMvc.perform(put("/api/saveBook/{bookId}", bookId)
                        .param("authorId", String.valueOf(authorId)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid operation"));

        verify(bookService, times(1)).savedBook(bookId, authorId);
    }
}
