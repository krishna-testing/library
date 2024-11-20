package org.clx.library.services;

import lombok.AllArgsConstructor;
import org.clx.library.exception.AuthorNotFoundException;
import org.clx.library.exception.BookNotFoundException;
import org.clx.library.exception.UnauthorizedBookDeletionException;
import org.clx.library.model.Author;
import org.clx.library.model.Book;
import org.clx.library.repositories.AuthorRepository;
import org.clx.library.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorService authorService;

    // Create a new book
    public Book createBook(Book book, Integer authorId) {
        logger.info("aCCE request to create book: {} for author with ID: {}", book.getName(), authorId);
        try {
            Author author = authorService.findAuthorById(authorId);

            Book newBook = new Book();
            newBook.setId(book.getId());
            newBook.setName(book.getName());
            newBook.setGenre(book.getGenre());
            newBook.setCreatedAt(LocalDateTime.now());
            newBook.setAvailable(book.getAvailable());
            newBook.setCard(book.getCard());
            newBook.setAuthor(author);

            Book savedBook = bookRepository.save(newBook);
            logger.info("Book created successfully with ID: {}", savedBook.getId());
            return savedBook;
        } catch (Exception e) {
            logger.error("Error occurred while creating book: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Delete a book
    public String deleteBook(Integer bookId, Integer authorId) {
        logger.info("Received request to delete book with ID: {} by author with ID: {}", bookId, authorId);
        try {
            Book book = findBookById(bookId);
            Author author = authorService.findAuthorById(authorId);

            // Check if the author is authorized to delete the book
            if (book.getAuthor().getId() != author.getId()) {
                logger.warn("Unauthorized attempt to delete book with ID: {} by author with ID: {}", bookId, authorId);
                throw new UnauthorizedBookDeletionException("You are not authorized to delete this book.");
            }
            bookRepository.delete(book);
            logger.info("Book with ID: {} deleted successfully", bookId);
            return "Post deleted Successfully";
        } catch (UnauthorizedBookDeletionException e) {
            logger.error("Unauthorized book deletion attempt: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deleting book with ID: {}: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }

    // Find a book by ID
    public Book findBookById(Integer bookId) {
        logger.info("Received request to find book with ID: {}", bookId);
        Optional<Book> opt = bookRepository.findById(bookId);
        if (opt.isEmpty()) {
            logger.error("Book with ID: {} not found", bookId);
            throw new BookNotFoundException("Book not found with this id: " + bookId);
        }
        logger.info("Book with ID: {} found", bookId);
        return opt.get();
    }

    // Get all books
    public List<Book> findAllBook() {
        logger.info("Received request to find all books");
        List<Book> books = bookRepository.findAll();
        logger.info("Found {} books", books.size());
        return books;
    }

    // Save or remove a book from an author's saved books
    public Book savedBook(Integer bookId, Integer authorId) throws AuthorNotFoundException, BookNotFoundException {
        logger.info("Received request to save/remove book with ID: {} for author with ID: {}", bookId, authorId);
        try {
            Book book = findBookById(bookId);
            Author author = authorService.findAuthorById(authorId);

            // Check if the book is already saved by the author
            if (author.getSavedBook().contains(book)) {
                // If the book is already in the list, remove it
                author.getSavedBook().remove(book);
                logger.info("Book with ID: {} removed from author's saved list", bookId);
            } else {
                // Otherwise, add the book to the list
                author.getSavedBook().add(book);
                logger.info("Book with ID: {} added to author's saved list", bookId);
            }

            // Save the updated author
            authorRepository.save(author);
            return book;
        } catch (Exception e) {
            logger.error("Error occurred while saving/removing book with ID: {}: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }

    // Get books based on filter criteria (genre, availability, author)
    public List<Book> getBooks(String genre, boolean isAvailable, String author) {
        logger.info("Received request to get books with genre: {}, availability: {}, author: {}", genre, isAvailable, author);

        List<Book> books;
        if (genre != null && author != null) {
            books = bookRepository.findBooksByGenre_Author(genre, author, isAvailable);
        } else if (genre != null) {
            books = bookRepository.findBooksByGenre(genre, isAvailable);
        } else if (author != null) {
            books = bookRepository.findBooksByAuthor(author, isAvailable);
        } else {
            books = bookRepository.findBooksByAvailability(isAvailable);
        }

        logger.info("Found {} books matching the criteria", books.size());
        return books;
    }
}
