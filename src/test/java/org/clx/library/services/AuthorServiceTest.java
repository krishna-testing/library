package org.clx.library.services;

import org.clx.library.dto.AuthorDto;
import org.clx.library.dto.AuthorRequest;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorRequest authorRequest;
    private Author author;
    private AuthorDto authorDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocking the AuthorRequest
        authorRequest = new AuthorRequest();
        authorRequest.setName("John Doe");
        authorRequest.setEmail("john.doe@example.com");
        authorRequest.setCountry("USA");

        // Mocking the Author entity
        author = new Author();
        author.setId(1);
        author.setName("John Doe");
        author.setEmail("john.doe@example.com");
        author.setCountry("USA");

        // Mocking the AuthorDto
        authorDto = new AuthorDto();
        authorDto.setId(1);
        authorDto.setName("John Doe");
        authorDto.setEmail("john.doe@example.com");
        authorDto.setCountry("USA");
    }

    @Test
     void testCreateAuthor() {
        // Arrange
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        // Act
        AuthorRequest result = authorService.createAuthor(authorRequest);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(authorRepository, times(1)).save(any(Author.class));
    }


    @Test
     void testFindAuthorById_AuthorNotFound() {
        // Arrange
        when(authorRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            authorService.findAuthorById(1);
        });

        // Assert the exception message matches the actual one
        assertEquals("Author not found with id : 1", thrown.getMessage());
    }

    @Test
     void testUpdateAuthor() throws ResourceNotFoundException {
        // Arrange
        AuthorRequest updatedRequest = new AuthorRequest();
        updatedRequest.setName("Jane Doe");
        updatedRequest.setEmail("jane.doe@example.com");
        updatedRequest.setCountry("Canada");

        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        // Act
        AuthorRequest result = authorService.updateAuthor(updatedRequest, 1);

        // Assert
        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("Canada", result.getCountry());
        verify(authorRepository, times(1)).findById(1);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
     void testUpdateAuthor_NotFound() {
        // Arrange
        AuthorRequest updatedRequest = new AuthorRequest();
        updatedRequest.setName("Jane Doe");

        when(authorRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            authorService.updateAuthor(updatedRequest, 1);
        });

        // Assert the exception message matches the actual one
        assertEquals("Author not found with id : 1", thrown.getMessage());
    }
    @Test
     void testDeleteAuthor() {
        // Arrange: Mock the repository method deleteCustom() to return an Integer, e.g., 1
        doReturn(1).when(authorRepository).deleteCustom(anyInt());

        // Act: Call deleteAuthor method
        authorService.deleteAuthor(1);

        // Assert: Verify the method was called once
        verify(authorRepository, times(1)).deleteCustom(1);
    }



    @Test
     void testUpdateAuthorDto() {
        when(authorRepository.updateAuthorDetails(any(Author.class))).thenReturn(1);

        // Act: Call updateAuthor
        AuthorDto result = authorService.updateAuthor(authorDto);

        // Assert: Verify the result is not null and the expected values are mapped correctly
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("USA", result.getCountry());

        // Assert: Verify that `updateAuthorDetails` was called exactly once
        verify(authorRepository, times(1)).updateAuthorDetails(any(Author.class));
    }
}
