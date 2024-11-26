package org.clx.library.services;

import org.clx.library.dto.AuthorDto;
import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;


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

}
