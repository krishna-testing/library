package org.clx.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clx.library.dto.BookDto;
import org.clx.library.model.Book;
import org.clx.library.services.BookService;
import org.clx.library.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Initialize MockMvc and ObjectMapper
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateBook_Success() throws Exception {
        // Arrange
        BookDto bookDto = new BookDto(null, "Book Title", "Author Name", "Genre");
        BookDto createdBook = new BookDto(1, "Book Title", "Author Name", "Genre");

        when(bookService.createBook(bookDto, 123)).thenReturn(createdBook);

        // Act and Assert
        mockMvc.perform(post("/api/authors/123/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author Name"));
    }

    @Test
    public void testCreateBook_Failure() throws Exception {
        // Arrange
        BookDto bookDto = new BookDto(null, "Book Title", "Author Name", "Genre");

        when(bookService.createBook(bookDto, 123)).thenThrow(new RuntimeException("Author not found"));

        // Act and Assert
        mockMvc.perform(post("/api/authors/123/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Author not found"));
    }

    @Test
    public void testDeleteBook_Success() throws Exception {
        // Arrange
        when(bookService.deleteBook(1, 123)).thenReturn("Book deleted successfully");

        // Act and Assert
        mockMvc.perform(delete("/api/deleteBook?bookId=1&authorId=123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Book deleted successfully"));
    }

    @Test
    public void testDeleteBook_Failure() throws Exception {
        // Arrange
        when(bookService.deleteBook(1, 123)).thenThrow(new RuntimeException("Book not found"));

        // Act and Assert
        mockMvc.perform(delete("/api/deleteBook?bookId=1&authorId=123"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Book not found"));
    }

    @Test
    public void testGetBooks_Success() throws Exception {
        // Arrange
        List<Book> books = List.of(new Book(1, "Book 1", "Author 1", "Genre 1"),
                new Book(2, "Book 2", "Author 2", "Genre 2",));
        when(bookService.getBooks("Genre 1", true, "Author 1")).thenReturn(books);

        // Act and Assert
        mockMvc.perform(get("/api/getBooks?genre=Genre 1&isAvailable=true&author=Author 1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetBooks_Failure() throws Exception {
        // Arrange
        when(bookService.getBooks("Genre 1", true, "Author 1")).thenThrow(new RuntimeException("Internal Server Error"));

        // Act and Assert
        mockMvc.perform(get("/api/getBooks?genre=Genre 1&isAvailable=true&author=Author 1"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Internal Server Error"));
    }

    @Test
    public void testGetBookById_Success() throws Exception {
        // Arrange
        BookDto bookDto = new BookDto(1, "Book Title", "Author Name", "Genre");

        when(bookService.findBookById(1)).thenReturn(bookDto);

        // Act and Assert
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book Title"));
    }

    @Test
    public void testGetBookById_Failure() throws Exception {
        // Arrange
        when(bookService.findBookById(1)).thenThrow(new ResourceNotFoundException("Book not found"));

        // Act and Assert
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    public void testUpdateBook_Success() throws Exception {
        // Arrange
        BookDto bookDto = new BookDto(1, "Updated Book Title", "Updated Author", "Updated Genre");
        when(bookService.updateBook(bookDto, 1)).thenReturn(bookDto);

        // Act and Assert
        mockMvc.perform(put("/api/book/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Book Title"));
    }

    @Test
    public void testUpdateBook_Failure() throws Exception {
        // Arrange
        BookDto bookDto = new BookDto(1, "Updated Book Title", "Updated Author", "Updated Genre");
        when(bookService.updateBook(bookDto, 1)).thenThrow(new RuntimeException("Book not found"));

        // Act and Assert
        mockMvc.perform(put("/api/book/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Book not found"));
    }
}
