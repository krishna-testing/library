package org.clx.library.book.service;

import org.clx.library.book.entities.Author;
import java.util.List;

public interface AuthorService {
    Author saveAuthor(Author author);
    Author updateAuthor(Author author);
    void deleteAuthor(Long id);
    Author getAuthorById(Long id);
    List<Author> getAllAuthors();
}
