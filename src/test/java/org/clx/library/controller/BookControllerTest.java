package org.clx.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clx.library.dto.BookDto;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
 class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @Autowired
    private ObjectMapper objectMapper;// Mocked BookService

    @MockBean
    private ModelMapper modelMapper; // Mocked ModelMapper

    private static final String BASE_URL = "/api";

    private BookDto bookDto;
    private BookDto createdBookDto;
    private BookDto updatedBookDto;

    @BeforeEach
    public void setUp() {
        bookDto = new BookDto();
        bookDto.setName("Book Title");

        createdBookDto = new BookDto();
        createdBookDto.setId(1);
        createdBookDto.setName("Book Title");

        updatedBookDto = new BookDto();
        updatedBookDto.setId(1);
        updatedBookDto.setName("Updated Book Title");
    }
    @Test
    void testCreateBook_ValidRequest() throws Exception {
        // Arrange: Mock the service call
        when(bookService.createBook(any(BookDto.class), eq(1))).thenReturn(createdBookDto);

        // Act & Assert: Perform POST request and check the response
        mockMvc.perform(post(BASE_URL + "/authors/{authorId}/books", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Book created successfully"))
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
     void testDeleteBook_Success() throws Exception {
        // Arrange: Mock the service call
        when(bookService.deleteBook(1, 1)).thenReturn("Book deleted successfully");

        // Act & Assert: Perform DELETE request and check the response
        mockMvc.perform(delete(BASE_URL + "/deleteBook")
                        .param("bookId", "1")
                        .param("authorId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book deleted successfully"));
    }@Test
     void testGetBookById_Success() throws Exception {
        // Arrange: Mock the service call
        when(bookService.findBookById(1)).thenReturn(createdBookDto);

        // Act & Assert: Perform GET request and check the response
        mockMvc.perform(get(BASE_URL + "/posts/{bookId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Book Title"));
    }

    @Test
     void testUpdateBook_Success() throws Exception {
        when(bookService.updateBook(bookDto, 1)).thenReturn(updatedBookDto);
        mockMvc.perform(put(BASE_URL + "/book/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book updated successfully"));
    }

    @Test
     void testGetBookById_NotFound() throws Exception {
        when(bookService.findBookById(99)).thenThrow(new ResourceNotFoundException("Book","id",99));
        mockMvc.perform(get(BASE_URL + "/posts/{bookId}", 99))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }


}
