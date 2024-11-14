package org.clx.library.services;

import lombok.AllArgsConstructor;
import org.clx.library.exception.AuthorException;
import org.clx.library.model.Author;
import org.clx.library.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author createAuthor(Author author) {
        Author newAuthor = new Author();
        newAuthor.setId(author.getId());
        newAuthor.setName(author.getName());
        newAuthor.setEmail(author.getEmail());
        newAuthor.setAge(author.getAge());
        newAuthor.setCountry(author.getCountry());
        newAuthor.setBooksWritten(author.getBooksWritten());
        return authorRepository.save(newAuthor);
    }
    public Author findAuthorById(Integer authorId) throws AuthorException {
        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent()) {
            return author.get();
        }
        throw new AuthorException("User not exist with userId" + authorId);
    }

    public Author updateAuthor(Author author, Integer authorId) throws AuthorException {
        Optional<Author> author1 = authorRepository.findById(authorId);
        if (author1.isEmpty()) {
            throw new AuthorException("User does not exist with id " + authorId);
        }
        Author oldAuthor = author1.get();
        if (author.getName() != null) {
            oldAuthor.setName(author.getName());
        }

        if (author.getEmail() != null) {
            oldAuthor.setEmail(author.getEmail());
        }

        return authorRepository.save(oldAuthor);
    }
    public void updateAuthor(Author author) {
        authorRepository.updateAuthorDetails(author);
    }

    public void deleteAuthor(int id) {
        authorRepository.deleteCustom(id);
    }
}