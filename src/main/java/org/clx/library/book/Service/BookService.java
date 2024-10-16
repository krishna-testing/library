package org.clx.library.book.Service;

import org.clx.library.book.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    List<Book> getAllBooks();

    Book getBookById(Long id);

    List<Book> searchBooksByTitle(String title);

    List<Book> searchBooksByAuthor(String author);

    Book saveBook(Book book);

    void deleteBookById(Long id);
}
