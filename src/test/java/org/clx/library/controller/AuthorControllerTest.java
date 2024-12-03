package org.clx.library.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.clx.library.dto.AuthorDto;
import org.clx.library.dto.AuthorRequest;
import org.clx.library.exception.ResourceNotFoundException;

import org.clx.library.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(AuthorController.class) // Only test the AuthorController
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private Logger logger;
    @MockBean
    private AuthorService authorService; // Mock the AuthorService

    private AuthorRequest authorRequest;
    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        authorRequest = new AuthorRequest();
        authorRequest.setName("John Doe");
        authorRequest.setEmail("john.doe@example.com");
        authorRequest.setCountry("USA");

        authorDto = new AuthorDto();
        authorDto.setId(1);
        authorDto.setName("John Doe");
        authorDto.setEmail("john.doe@example.com");
        authorDto.setCountry("USA");
    }

    @Test
    void testCreateAuthor_Success() throws Exception {
        when(authorService.createAuthor(any(AuthorRequest.class))).thenReturn(authorRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/createAuthor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\", \"email\":\"john.doe@example.com\", \"country\":\"USA\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Author created successfully"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(authorService, times(1)).createAuthor(any(AuthorRequest.class));
    }

    @Test
    void testUpdateAuthor_Success() throws Exception {
        AuthorDto authorDto1 = new AuthorDto();
        authorDto1.setId(1);
        authorDto1.setName("John Doe");
        authorDto1.setEmail("john.doe@example.com");
        authorDto1.setCountry("USA");

        when(authorService.updateAuthor(any(AuthorRequest.class), eq(1))).thenReturn(authorRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/updateAuthor/{authorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\", \"email\":\"john.doe@example.com\", \"country\":\"USA\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("Author updated successfully"))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.data.country").value("USA"));

        verify(authorService, times(1)).updateAuthor(any(AuthorRequest.class), eq(1));
    }

    @Test
    void testUpdateAuthor_AuthorNotFound() throws Exception {
        when(authorService.updateAuthor(any(AuthorRequest.class), eq(999))).thenThrow(new ResourceNotFoundException("Author", "id", 999));

        mockMvc.perform(MockMvcRequestBuilders.put("/updateAuthor/{authorId}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\", \"email\":\"john.doe@example.com\", \"country\":\"USA\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Author not found"));

        verify(authorService, times(1)).updateAuthor(any(AuthorRequest.class), eq(999));
    }


    @Test
    void testDeleteAuthor_Error() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Failed to delete author")).when(authorService).deleteAuthor(anyInt());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAuthor/{id}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("failed"));

        verify(authorService, times(1)).deleteAuthor((1));
    }

    @Test
    void testDeleteAuthor_Success() throws Exception {
        int authorId = 1;

        // Mock the service
        doNothing().when(authorService).deleteAuthor(authorId);

        // Perform the request
        mockMvc.perform(delete("/deleteAuthor/{id}", authorId))
                .andExpect(status().isNoContent())
                .andDo(print());

        // Verify the service method was called
        verify(authorService, times(1)).deleteAuthor(authorId);
    }


    @Test
    void testFindAuthorById_Success() throws Exception {
        when(authorService.findAuthorById(anyInt())).thenReturn(authorDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/findAuthor/{authorId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Author found successfully"))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(authorService, times(1)).findAuthorById((1));
    }

    @Test
    void testFindAuthorById_AuthorNotFound() throws Exception {
        when(authorService.findAuthorById(anyInt())).thenThrow(new ResourceNotFoundException("Author", "id", 999));

        mockMvc.perform(MockMvcRequestBuilders.get("/findAuthor/{authorId}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("failed"))
                .andExpect(jsonPath("$.data").value("Author not found with id : 999"));

        verify(authorService, times(1)).findAuthorById((999));
    }


}
