package org.clx.library.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.exception.AuthorNotFoundException;
import org.clx.library.exception.BookNotFoundException;
import org.clx.library.exception.UnauthorizedBookDeletionException;
import org.clx.library.model.Author;
import org.clx.library.model.Book;
import org.clx.library.model.Genre;
import org.clx.library.repositories.AuthorRepository;
import org.clx.library.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class BookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public Book createBook(Book book, Integer authorId) {
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
        log.info("Author created successfully with ID: {}", savedBook.getId());
        return savedBook;
    }

    public String deleteBook(Integer bookId, Integer authorId) {
        Book book = findBookById(bookId);
        Author author = authorService.findAuthorById(authorId);

        if (book.getAuthor().getId() != author.getId()){
            throw new UnauthorizedBookDeletionException("You are not authorized to delete this book.");
        }
        bookRepository.delete(book);
        log.info("Author deleted  successfully with ID: {}", authorId);
        return "Post deleted Successfully";
    }

    public Book findBookById(Integer bookId) {
        Optional<Book> opt = bookRepository.findById(bookId);
        if (opt.isEmpty()) {
            log.info("Book found with this ID : {}", bookId);
            throw new BookNotFoundException("Book not found with this id: " + bookId);
        }
        return opt.get();
    }


    public List<Book> findAllBook() {
        log.info("Found All Book ");
        return bookRepository.findAll();
    }

    public Book savedBook(Integer bookId, Integer authorId) throws AuthorNotFoundException,BookNotFoundException {
        Book book = findBookById(bookId);
        Author author = authorService.findAuthorById(authorId);
        // Check if the book is already saved by the author
        if (author.getSavedBook().contains(book)) {
            // If the book is already in the list, remove it
            author.getSavedBook().remove(book);
        } else {
            // Otherwise, add the book to the list
            author.getSavedBook().add(book);
        }

        // Save the updated author
        authorRepository.save(author);

        return book;
    }

    //Either giving you all the available books
    //OR gicing you all the non-available books
    public List<Book> getBooks(String genre, boolean isAvailable, String author) {
        log.info("Received request to fetch books with the following filters - Genre: {}, Available: {}, Author: {}", genre, isAvailable, author);
        if (genre != null && author != null) {
            log.info("Fetching books with genre: {} and author: {}", Genre.valueOf(genre), author);
            return bookRepository.findBooksByGenre_Author(Genre.valueOf(genre), author, isAvailable);
        } else if (genre != null) {
            log.info("Fetching books with genre: {}", Genre.valueOf(genre));
            return bookRepository.findBooksByGenre(Genre.valueOf(genre), isAvailable);
        } else if (author != null) {
            log.info("Fetching books by author: {}", author);
            return bookRepository.findBooksByAuthor(author, isAvailable);
        }
        log.info("Fetching books based on availability: {}", isAvailable);
        return bookRepository.findBooksByAvailability(isAvailable);

    }

}
