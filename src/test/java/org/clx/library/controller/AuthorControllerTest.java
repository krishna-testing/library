package org.clx.library.controller;

import org.clx.library.dto.AuthorDto;
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

    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();

        // Initialize the AuthorDto object
        authorDto = new AuthorDto();
        authorDto.setId(1);
        authorDto.setName("Author Name");
        authorDto.setEmail("author@example.com");
        authorDto.setAge(45);
        authorDto.setCountry("Country");
    }

    @Test
    void testCreateAuthor() throws Exception {
        // Arrange
        when(authorService.createAuthor(any(AuthorDto.class))).thenReturn(authorDto);

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
        AuthorDto updatedAuthorDto = new AuthorDto();
        updatedAuthorDto.setName("Updated Name");
        updatedAuthorDto.setEmail("updated@example.com");

        when(authorService.updateAuthor(any(AuthorDto.class), eq(1))).thenReturn(updatedAuthorDto);

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
        when(authorService.updateAuthor(any(AuthorDto.class), eq(1)))
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
        mockMvc.perform(delete("/deleteAuthor/{id}", 1))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Author deleted!!"));

        // Verify that deleteAuthor method was called once
        verify(authorService, times(1)).deleteAuthor(1);
    }
}
