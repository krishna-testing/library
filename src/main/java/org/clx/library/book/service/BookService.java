package org.clx.library.book.service;

import org.clx.library.book.entities.Book;
import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    Book updateBook(Book book);
    void deleteBook(Long id);
    Book getBookById(Long id);
    List<Book> getAllBooks();
}
