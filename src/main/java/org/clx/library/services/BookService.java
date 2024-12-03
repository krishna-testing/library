package org.clx.library.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.dto.AuthorDto;
import org.clx.library.dto.BookDto;
import org.clx.library.payload.BookResponse;

import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.exception.UnauthorizedBookDeletionException;
import org.clx.library.model.Author;
import org.clx.library.model.Book;
import org.clx.library.model.Genre;
import org.clx.library.repositories.AuthorRepository;
import org.clx.library.repositories.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;


@Service
@Slf4j
@AllArgsConstructor
public class BookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private ModelMapper modelMapper;

    public BookDto createBook(BookDto bookDto, int authorId) {

        Author author = this.authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author ", "Author id", authorId));


        Book book = this.modelMapper.map(bookDto, Book.class);
        book.setId(book.getId());
        book.setName(book.getName());
        book.setGenre(book.getGenre());
        book.setCreatedAt(LocalDateTime.now());
        book.setAvailable(book.getAvailable());
        book.setCard(book.getCard());
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        log.info("Author created successfully with ID: {}", savedBook.getId());

        return this.modelMapper.map(book, BookDto.class);
    }

    // If you need to have another method with a different signature, modify it like this
    public String deleteBook(Integer bookId, Integer authorId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("book", "book id", bookId));
        AuthorDto author = authorService.findAuthorById(authorId);
        if (book.getAuthor().getId() != author.getId()) {
            throw new UnauthorizedBookDeletionException("You are not authorized to delete this book.");
        }

        bookRepository.delete(book);
        log.info("Book deleted successfully with ID: {}", bookId);
        return "Book deleted successfully";
    }


    public BookDto findBookById(Integer bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "book id", bookId));
        return this.modelMapper.map(book, BookDto.class);
    }


    public BookDto updateBook(BookDto bookDto, int bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("post", "POST_ID_FIELD", bookId));
        book.setName(bookDto.getName());
        book.setGenre(bookDto.getGenre());
        book.setCreatedAt(LocalDateTime.now());
        Book updatedBook = this.bookRepository.save(book);
        return this.modelMapper.map(updatedBook, BookDto.class);
    }


    public BookResponse findAllBook(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort /* Sort.by(sortBy).descending() */);
        Page<Book> pagePost = this.bookRepository.findAll(p);
        List<Book> allPosts = pagePost.getContent();
        List<BookDto> postDtos = allPosts.stream().map(post -> this.modelMapper.map(post, BookDto.class))
                .toList();
        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(postDtos);
        bookResponse.setPageNumber(pagePost.getNumber());
        bookResponse.setPageSize(pagePost.getSize());
        bookResponse.setTotalElements(pagePost.getTotalElements());
        bookResponse.setTotalPages(pagePost.getTotalPages());
        bookResponse.setLastPage(pagePost.isLast());

        return bookResponse;
    }

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
