package org.clx.library.services;

import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AuthorServiceTest {

    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createAuthorTest() {
        Author author = new Author("John Doe", "john.doe@example.com", 45, "USA");
        doNothing().when(authorRepository).save(author);

        authorService.createAuthor(author);

        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void updateAuthorTest() {
        Author author = new Author("Jane Doe", "jane.doe@example.com", 35, "Canada");
        author.setId(1);
        when(authorRepository.updateAuthorDetails(author)).thenReturn(1);

        authorService.updateAuthor(author);

        verify(authorRepository, times(1)).updateAuthorDetails(author);
    }

    @Test
    void deleteAuthorTest() {
        int authorId = 1;
        doNothing().when(authorRepository).deleteCustom(authorId);

        authorService.deleteAuthor(authorId);

        verify(authorRepository, times(1)).deleteCustom(authorId);
    }
}