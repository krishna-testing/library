//package org.clx.library.bookabhay.service.ServieImpl;
//
//import lombok.AllArgsConstructor;
//import org.clx.library.bookabhay.entities.Book;
//import org.clx.library.bookabhay.repositories.BookRepository;
//import org.clx.library.bookabhay.service.AbhayBookService;
//import org.clx.library.user.exception.UserException;
//import org.clx.library.user.model.User;
//import org.clx.library.user.repository.UserRepository;
//import org.clx.library.user.service.UserService;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//public class AbhayBookServiceImpl implements AbhayBookService {
//
//    private final BookRepository bookRepository;
//    private final UserService userService;
//    private final UserRepository userRepository;
//
//    @Override
//    public Book createBook(Book book, Integer userId) throws UserException {
//        User user = userService.findUserById(userId);
//        book.setCreatedAt(LocalDateTime.now());
//        book.setUser(user);
//        return bookRepository.save(book);
//    }
//
//    @Override
//    public String deleteBook(Integer bookId, Integer userId) throws Exception {
//        Book book = findBookById(bookId);
//        if (!book.getUser().getId().equals(userId)) {
//            throw new Exception("You don't have permission to delete this book");
//        }
//        bookRepository.delete(book);
//        return "Book deleted successfully";
//    }
//
//    @Override
//    public List<Book> findBookByUserId(Integer userId) {
//        return bookRepository.findBookByUserId(userId);
//    }
//
//    @Override
//    public Book findBookById(Integer bookId) throws Exception {
//        Optional<Book> bookOpt = bookRepository.findById(bookId);
//        if (bookOpt.isEmpty()) {
//            throw new Exception("Book not found with ID: " + bookId);
//        }
//        return bookOpt.get();
//    }
//
//    @Override
//    public List<Book> getAllBooks() {
//        return bookRepository.findAll();
//    }
//    @Override
//    public Book updateBook(Book book) {
//        return bookRepository.save(book);
//    }
//}
