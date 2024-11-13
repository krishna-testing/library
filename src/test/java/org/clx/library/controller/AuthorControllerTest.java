package org.clx.library.controller;

import org.clx.library.model.Author;
import org.clx.library.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @Test
    public void createAuthorTest() throws Exception {
        Author author = new Author(1,"John Doe", "john.doe@example.com", 45, "USA",null);

        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        doNothing().when(authorService).createAuthor(any(Author.class));

        mockMvc.perform(post("/createAuthor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"age\":45,\"country\":\"USA\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Author created"));

        verify(authorService, times(1)).createAuthor(any(Author.class));
    }

    @Test
    public void updateAuthorTest() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        doNothing().when(authorService).updateAuthor(any(Author.class));

        mockMvc.perform(put("/updateAuthor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\",\"age\":35,\"country\":\"Canada\"}"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Author updated!!"));

        verify(authorService, times(1)).updateAuthor(any(Author.class));
    }

    @Test
    public void deleteAuthorTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        int authorId = 1;

        doNothing().when(authorService).deleteAuthor(authorId);

        mockMvc.perform(delete("/deleteAuthor")
                        .param("id", String.valueOf(authorId)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Author deleted!!"));

        verify(authorService, times(1)).deleteAuthor(authorId);
    }
}
