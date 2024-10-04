package org.clx.library.bookabhay.service.ServieImpl;

import lombok.AllArgsConstructor;
import org.clx.library.bookabhay.entities.Book;
import org.clx.library.bookabhay.repositories.BookRepository;
import org.clx.library.bookabhay.service.BookService;
import org.clx.library.user.exception.UserException;
import org.clx.library.user.model.User;
import org.clx.library.user.repository.UserRepository;
import org.clx.library.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final UserRepository userRepository;


    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book createBook(Book book, Integer userid) throws UserException {
        User user = userService.findUserById(userid);
        Book newBook = new Book();
        newBook.setISBN(book.getISBN());
        newBook.setTitle(book.getTitle());
        newBook.setStatus(book.getStatus());
//        newBook.setAuthor(book.getAuthor( ));
        newBook.setCreatedAt(LocalDateTime.now());
        newBook.setUser(user);

        return bookRepository.save(newBook);
    }

    @Override
    public String deleteBook(Integer bookId, Integer userId) throws Exception {

        Book book = findBookById(bookId);
        User user = userService.findUserById(userId);
        if (book.getUser().getId() != user.getId()) {
            throw new Exception("You can't delete Unother post ");
        }
        bookRepository.delete(book);
        return "Post deleted Successfully";
    }

    @Override
    public List<Book> findBookByUserId(Integer userId) {
        return bookRepository.findBookByUserId(userId);
    }

    @Override
    public Book findBookById(Integer bookId) throws Exception {
        Optional<Book> opt = bookRepository.findById(bookId);
        if (opt.isEmpty()) {
            throw new Exception("Post not Found with this id" + bookId);
        }
        return opt.get();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book savedBook(Integer bookId, Integer userId) throws Exception {
        Book book = findBookById(bookId);
        User user = userService.findUserById(userId);

        if (user.getSavedPost().contains(book)) {
            user.getSavedPost().remove(book);
        } else {
            user.getSavedPost().add(book);
        }
        userRepository.save(user);
        return book;
    }
}

