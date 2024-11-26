package org.clx.library.services;

import org.clx.library.dto.AuthorDto;
import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        // Initialize the Author object
        author = new Author();
        author.setId(1);
        author.setName("Author Name");
        author.setEmail("author@example.com");
        author.setAge(45);
        author.setCountry("Country");

        // Initialize the AuthorDto object
        authorDto = new AuthorDto();
        authorDto.setId(1);
        authorDto.setName("Author Name");
        authorDto.setEmail("author@example.com");
        authorDto.setAge(45);
        authorDto.setCountry("Country");
    }

    @Test
    void testCreateAuthor() {
        // Arrange
        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);

        // Act
        AuthorDto createdAuthorDto = authorService.createAuthor(authorDto);

        // Assert
        assertNotNull(createdAuthorDto);
        assertEquals("Author Name", createdAuthorDto.getName());
        assertEquals("author@example.com", createdAuthorDto.getEmail());
        verify(authorRepository, times(1)).save(Mockito.any(Author.class));
    }

    @Test
    void testFindAuthorById_Success() throws AuthorNotFoundException {
        // Arrange
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        // Act
        AuthorDto foundAuthorDto = authorService.findAuthorById(1);

        // Assert
        assertNotNull(foundAuthorDto);
        assertEquals(1, foundAuthorDto.getId());
        assertEquals("Author Name", foundAuthorDto.getName());
    }

    @Test
    void testFindAuthorById_NotFound() {
        // Arrange
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class, () -> {
            authorService.findAuthorById(1);
        });
        assertEquals("User does not exist with userId: 1", exception.getMessage());
    }

    @Test
    void testUpdateAuthor_Success() throws AuthorNotFoundException {
        // Arrange
        AuthorDto updatedAuthorDto = new AuthorDto();
        updatedAuthorDto.setName("Updated Name");
        updatedAuthorDto.setEmail("updated@example.com");

        Author existingAuthor = new Author();
        existingAuthor.setId(1);
        existingAuthor.setName("Old Name");
        existingAuthor.setEmail("old@example.com");

        when(authorRepository.findById(1)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(existingAuthor);

        // Act
        AuthorDto updatedAuthorDtoResult = authorService.updateAuthor(updatedAuthorDto, 1);

        // Assert
        assertNotNull(updatedAuthorDtoResult);
        assertEquals("Updated Name", updatedAuthorDtoResult.getName());
        assertEquals("updated@example.com", updatedAuthorDtoResult.getEmail());
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void testUpdateAuthor_NotFound() {
        // Arrange
        AuthorDto updatedAuthorDto = new AuthorDto();
        updatedAuthorDto.setName("Updated Name");

        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class, () -> {
            authorService.updateAuthor(updatedAuthorDto, 1);
        });
        assertEquals("User does not exist with ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateAuthorDetails() {
        // Arrange
        AuthorDto updatedAuthorDto = new AuthorDto();
        updatedAuthorDto.setName("Updated Author Name");

        when(authorRepository.updateAuthorDetails(Mockito.any(Author.class))).thenReturn(1); // Assume update returns 1 on success

        // Act
        AuthorDto result = authorService.updateAuthor(updatedAuthorDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Author Name", result.getName());
        verify(authorRepository, times(1)).updateAuthorDetails(Mockito.any(Author.class));
    }

    @Test
    void testDeleteAuthor_Success() {
        // Arrange
        doNothing().when(authorRepository).deleteCustom(1);

        // Act
        authorService.deleteAuthor(1);

        // Assert
        verify(authorRepository, times(1)).deleteCustom(1);
    }

    @Test
    void testDeleteAuthor_Exception() {
        // Arrange
        doThrow(new RuntimeException("Error deleting author")).when(authorRepository).deleteCustom(1);

        // Act & Assert
        try {
            authorService.deleteAuthor(1);
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertEquals("Error deleting author", e.getMessage());
        }

        verify(authorRepository, times(1)).deleteCustom(1);
    }
}
