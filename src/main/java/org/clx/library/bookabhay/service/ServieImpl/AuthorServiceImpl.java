//package org.clx.library.book.service.ServieImpl;
//
//
//import org.clx.library.book.entities.Author;
//import org.clx.library.book.repositories.AuthorRepository;
//import org.clx.library.book.service.AuthorService;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class AuthorServiceImpl implements AuthorService {
//
//    private final AuthorRepository authorRepository;
//
//    public AuthorServiceImpl(AuthorRepository authorRepository) {
//        this.authorRepository = authorRepository;
//    }
//
//    @Override
//    public Author saveAuthor(Author author) {
//        return authorRepository.save(author);
//    }
//
//    @Override
//    public Author updateAuthor(Author author) {
//        return authorRepository.save(author);
//    }
//
//    @Override
//    public void deleteAuthor(Integer id) {
//        authorRepository.deleteById(id);
//    }
//
//    @Override
//    public Author getAuthorById(Integer id) {
//        return authorRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public List<Author> getAllAuthors() {
//        return authorRepository.findAll();
//    }
//}
