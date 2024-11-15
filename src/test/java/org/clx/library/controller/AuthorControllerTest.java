package org.clx.library.controller;

import org.clx.library.exception.AuthorNotFoundException;
import org.clx.library.model.Author;
import org.clx.library.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;

    private Author author;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();

        // Initialize the Author object
        author = new Author();
        author.setId(1);
        author.setName("Author Name");
        author.setEmail("author@example.com");
        author.setAge(45);
        author.setCountry("Country");
    }

    @Test
    void testCreateAuthor() throws Exception {
        // Arrange
        when(authorService.createAuthor(any(Author.class))).thenReturn(author);

        // Act & Assert
        mockMvc.perform(post("/createAuthor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Author Name\", \"email\": \"author@example.com\", \"age\": 45, \"country\": \"Country\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Author created with ID: 1"));
    }

    @Test
    void testUpdateAuthor_Success() throws Exception {
        // Arrange
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Name");
        updatedAuthor.setEmail("updated@example.com");

        when(authorService.updateAuthor(any(Author.class), eq(1))).thenReturn(updatedAuthor);

        // Act & Assert
        mockMvc.perform(put("/updateAuthor/{authorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Name\", \"email\": \"updated@example.com\"}"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Author updated!!"));
    }

    @Test
    void testUpdateAuthor_NotFound() throws Exception {
        // Arrange
        when(authorService.updateAuthor(any(Author.class), eq(1)))
                .thenThrow(new AuthorNotFoundException("Author with ID 1 not found"));

        // Act & Assert
        mockMvc.perform(put("/updateAuthor/{authorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Name\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Author with ID 1 not found"));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        // Arrange
        doNothing().when(authorService).deleteAuthor(1);

        // Act & Assert
        mockMvc.perform(delete("/{id}", 1))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Author deleted!!"));

        // Verify that deleteAuthor method was called once
        verify(authorService, times(1)).deleteAuthor(1);
    }
}
