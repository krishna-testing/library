package org.clx.library.bookabhay.service;

import org.clx.library.bookabhay.entities.Author;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface AuthorService {
    Author saveAuthor(Author author);
    Author updateAuthor(Author author);
    void deleteAuthor(Integer id);
    Author getAuthorById(Integer id);
    List<Author> getAllAuthors();
}
