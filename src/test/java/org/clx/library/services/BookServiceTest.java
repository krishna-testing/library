package org.clx.library.services;

import org.clx.library.model.Book;
import org.clx.library.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;

import java.time.LocalDateTime;

class BookServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Author and Book objects
        author = new Author();
        author.setId(1);
        author.setName("Author Name");

        book = new Book();
        book.setId(1);
        book.setName("Book Title");
        book.setAuthor(author);
        book.setCreatedAt(LocalDateTime.now());
    }


}
