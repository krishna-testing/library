package org.clx.library.bookabhay.service;

import org.clx.library.bookabhay.entities.Book;
import org.clx.library.user.exception.UserException;

import java.util.List;

public interface BookService {
    Book createBook(Book book, Integer userid) throws UserException;
    String deleteBook( Integer bookId, Integer userId) throws Exception;
    List<Book> findBookByUserId(Integer userId);
    Book findBookById(Integer bookId) throws Exception;
    List<Book> getAllBooks();
    Book savedBook(Integer bookId, Integer userId) throws Exception;

    Book updateBook(Book book);
}
