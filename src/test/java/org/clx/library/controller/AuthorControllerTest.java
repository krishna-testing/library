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


}
