package org.clx.library.services;

import lombok.AllArgsConstructor;
import org.clx.library.controller.AuthorController;
import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

@WebMvcTest(AuthorController.class)
@AllArgsConstructor
public class AuthorControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @Test
    public void createAuthorTest() throws Exception {
        Author author = new Author(1, "John Doe", "john.doe@example.com", 45, "USA", null);

        doNothing().when(authorService).createAuthor(any(Author.class));

        // Perform POST request with JSON content
        mockMvc.perform(post("/authors")  // changed the endpoint to /authors
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"age\":45,\"country\":\"USA\"}"))
                .andExpect(status().isCreated())  // 201 Created
                .andExpect(content().string("Author created"));

        verify(authorService, times(1)).createAuthor(any(Author.class));
    }

    @Test
    public void updateAuthorTest() throws Exception {
        Author author = new Author(1, "Jane Doe", "jane.doe@example.com", 35, "Canada", null);

        doNothing().when(authorService).updateAuthor(any(Author.class));

        // Perform PUT request with JSON content
        mockMvc.perform(put("/authors/1")  // updated endpoint with author ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\",\"age\":35,\"country\":\"Canada\"}"))
                .andExpect(status().isAccepted())  // 202 Accepted
                .andExpect(content().string("Author updated!!"));

        verify(authorService, times(1)).updateAuthor(any(Author.class));
    }

    @Test
    public void deleteAuthorTest() throws Exception {
        int authorId = 1;

        doNothing().when(authorService).deleteAuthor(authorId);

        // Perform DELETE request with author ID
        mockMvc.perform(delete("/authors/{id}", authorId))  // endpoint updated to /authors/{id}
                .andExpect(status().isNoContent())  // 204 No Content (common for successful deletes)
                .andExpect(content().string("Author deleted!!"));

        verify(authorService, times(1)).deleteAuthor(authorId);
    }
}