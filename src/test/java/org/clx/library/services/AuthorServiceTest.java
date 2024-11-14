package org.clx.library.services;

import org.clx.library.exception.AuthorException;
import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the Author object
        author = new Author();
        author.setId(1);
        author.setName("Author Name");
        author.setEmail("author@example.com");
        author.setAge(45);
        author.setCountry("Country");
    }

    @Test
    void testCreateAuthor() {
        // Arrange
        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);

        // Act
        Author createdAuthor = authorService.createAuthor(author);

        // Assert
        assertNotNull(createdAuthor);
        assertEquals("Author Name", createdAuthor.getName());
        assertEquals("author@example.com", createdAuthor.getEmail());
        verify(authorRepository, times(1)).save(Mockito.any(Author.class));
    }

    @Test
    void testFindAuthorById_Success() throws AuthorException {
        // Arrange
        Integer authorId = 1;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        // Act
        Author foundAuthor = authorService.findAuthorById(authorId);

        // Assert
        assertNotNull(foundAuthor);
        assertEquals(authorId, foundAuthor.getId());
    }

    @Test
    void testFindAuthorById_NotFound() {
        // Arrange
        Integer authorId = 1;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.findAuthorById(authorId);
        });

        assertEquals("User not exist with userId1", exception.getMessage());
    }

    @Test
    void testUpdateAuthor_Success() throws AuthorException {
        // Arrange
        Integer authorId = 1;
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Name");
        updatedAuthor.setEmail("updated@example.com");

        Author existingAuthor = new Author();
        existingAuthor.setId(1);
        existingAuthor.setName("Old Name");
        existingAuthor.setEmail("old@example.com");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(existingAuthor);

        // Act
        Author updated = authorService.updateAuthor(updatedAuthor, authorId);

        // Assert
        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
        assertEquals("updated@example.com", updated.getEmail());
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void testUpdateAuthor_NotFound() {
        // Arrange
        Integer authorId = 1;
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Name");

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.updateAuthor(updatedAuthor, authorId);
        });

        assertEquals("User does not exist with id 1", exception.getMessage());
    }

    @Test
    void testUpdateAuthor_NoFieldsUpdated() throws AuthorException {
        // Arrange
        Integer authorId = 1;
        Author updatedAuthor = new Author();
        updatedAuthor.setName(null);
        updatedAuthor.setEmail(null);

        Author existingAuthor = new Author();
        existingAuthor.setId(1);
        existingAuthor.setName("Old Name");
        existingAuthor.setEmail("old@example.com");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(existingAuthor);

        // Act
        Author updated = authorService.updateAuthor(updatedAuthor, authorId);

        // Assert
        assertNotNull(updated);
        assertEquals("Old Name", updated.getName());
        assertEquals("old@example.com", updated.getEmail());
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void testUpdateAuthorDetails() {
        // Arrange
        author.setName("Updated Author Name");
        when(authorRepository.updateAuthorDetails(Mockito.any(Author.class))).thenReturn(1); // Assume update returns 1 on success

        // Act
        authorService.updateAuthor(author);

        // Assert
        verify(authorRepository, times(1)).updateAuthorDetails(author);
    }

    @Test
    void testDeleteAuthor() {
        // Arrange
        int authorId = 1;
        doNothing().when(authorRepository).deleteCustom(authorId);

        // Act
        authorService.deleteAuthor(authorId);

        // Assert
        verify(authorRepository, times(1)).deleteCustom(authorId);
    }
}
