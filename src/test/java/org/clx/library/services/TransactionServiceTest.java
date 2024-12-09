package org.clx.library.services;

import org.clx.library.exception.MaxAllowedBooksException;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.model.*;
import org.clx.library.repositories.BookRepository;
import org.clx.library.repositories.CardRepository;
import org.clx.library.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testIssueBook_BookNotFound() {
        // Arrange
        int cardId = 1;
        int bookId = 100;

        // Mock the behavior of bookRepository
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.issueBooks(cardId, bookId);
        });

        // Assertions
        assertEquals("Book not found with id : " + bookId, exception.getMessage());
    }

    @Test
    void testIssueBook_BookUnavailable() {
        // Arrange
        int cardId = 1;
        int bookId = 100;

        // Create a mock book object
        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(false); // Set the book as unavailable

        // Mock the behavior of bookRepository
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.issueBooks(cardId, bookId);
        });

        // Assertions
        assertEquals("Book not found with id : "+bookId, exception.getMessage());
    }

    @Test
    void testIssueBook_CardNotFound() {
        // Arrange
        int cardId = 1;
        int bookId = 100;

        // Mock the behavior of bookRepository and cardRepository
        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.issueBooks(cardId, bookId);
        });

        // Assertions
        assertEquals("Card not found with id : " + cardId, exception.getMessage());
    }

    @Test
    void testIssueBook_CardDeactivated() {
        // Arrange
        int cardId = 1;
        int bookId = 100;

        // Create a mock card and book object
        Card card = new Card();
        card.setId(cardId);
        card.setCardStatus(CardStatus.DEACTIVATED); // Set the card as deactivated
        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);

        // Mock the behavior of cardRepository and bookRepository
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.issueBooks(cardId, bookId);
        });

        // Assertions
        assertEquals("Card not found with id : "+cardId, exception.getMessage());
    }

    @Test
    void testIssueBook_MaxAllowedBooksExceeded() {
        // Arrange
        int cardId = 1;
        int bookId = 100;
        int maxAllowedBooks = 3;  // This should match the value used in the service

        // Create a mock card and book object
        Card card = new Card();
        card.setId(cardId);
        card.setCardStatus(CardStatus.ACTIVATED);

        // Set the card's books list to exceed maxAllowedBooks
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < maxAllowedBooks; i++) {
            books.add(new Book());  // Add maxAllowedBooks number of books
        }
        card.setBooks(books);  // Set the card to have exactly maxAllowedBooks books

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true); // The book is available for issuing

        // Mock the behavior of bookRepository and cardRepository
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act & Assert
        MaxAllowedBooksException exception = assertThrows(MaxAllowedBooksException.class, () -> {
            transactionService.issueBooks(cardId, bookId);
        });

        // Assertions
        assertEquals("Book limit reached for this card!!", exception.getMessage());
    }

    @Test
    void testIssueBook_Success() {
        // Arrange
        int cardId = 1;
        int bookId = 100;

        // Create a mock card and book object
        Card card = new Card();
        card.setId(cardId);
        card.setCardStatus(CardStatus.ACTIVATED);
        card.setBooks(new ArrayList<>()); // No books yet
        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);

        // Mock the behavior of bookRepository and cardRepository
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Create an ArgumentCaptor for capturing the Transaction object
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(transactionRepository.save(transactionCaptor.capture())).thenReturn(new Transaction());

        // Act
        transactionService.issueBooks(cardId, bookId);

        // Assert: Verify the correct transaction ID was generated
        Transaction capturedTransaction = transactionCaptor.getValue();
        assertNotNull(capturedTransaction.getTransactionId()); // Ensure the transaction ID is not null

        // You can also check if the transaction ID matches a pattern (e.g., UUID)
        assertTrue(capturedTransaction.getTransactionId().matches("[a-f0-9\\-]{36}")); // Example UUID format

        // Verify that the appropriate methods were called
        verify(bookRepository, times(1)).updateBook(book);
        verify(transactionRepository, times(1)).save(Mockito.any(Transaction.class));
    }


    @Test
    void testReturnBook_Success() {
        // Arrange
        int cardId = 1;
        int bookId = 100;

        // Create a mock card and book object
        Card card = new Card();
        card.setId(cardId);
        card.setCardStatus(CardStatus.ACTIVATED);

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(false);
        book.setCard(card);  // The book is associated with the card

        // Create a mock transaction object for issuing the book
        Transaction issuedTransaction = new Transaction();
        issuedTransaction.setCard(card);
        issuedTransaction.setBook(book);
        issuedTransaction.setIsIssueOperation(true);
        issuedTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        issuedTransaction.setTransactionDate(new Date());

        // Mock the behavior of repositories
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(transactionRepository.findByCard_Book(cardId, bookId, TransactionStatus.SUCCESSFUL, true))
                .thenReturn(Collections.singletonList(issuedTransaction));

        // Act
        String transactionId = transactionService.returnBooks(cardId, bookId);

        // Assert
        assertNotNull(transactionId);
        verify(bookRepository, times(1)).save(book);
        verify(transactionRepository, times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    void testReturnBook_LateReturnWithFine() {
        // Arrange
        int cardId = 1;
        int bookId = 100;

        // Create a mock card and book object
        Card card = new Card();
        card.setId(cardId);
        card.setCardStatus(CardStatus.ACTIVATED);

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(false);
        book.setCard(card);  // The book is associated with the card

        // Create a mock transaction object for issuing the book (late issue)
        Transaction issuedTransaction = new Transaction();
        issuedTransaction.setCard(card);
        issuedTransaction.setBook(book);
        issuedTransaction.setIsIssueOperation(true);
        issuedTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        issuedTransaction.setTransactionDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)));  // Simulate a late issue (10 days late)

        // Mock the behavior of repositories
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(transactionRepository.findByCard_Book(cardId, bookId, TransactionStatus.SUCCESSFUL, true))
                .thenReturn(Collections.singletonList(issuedTransaction));

        // Act
        String transactionId = transactionService.returnBooks(cardId, bookId);

        // Assert
        assertNotNull(transactionId);
        verify(bookRepository, times(1)).save(book);
        verify(transactionRepository, times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    void testReturnBook_NoActiveIssueTransaction() {
        // Arrange
        int cardId = 1;
        int bookId = 100;

        // Create a mock card and book object
        Card card = new Card();
        card.setId(cardId);
        card.setCardStatus(CardStatus.ACTIVATED);

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);  // The book is available

        // Mock the behavior of repositories
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(transactionRepository.findByCard_Book(cardId, bookId, TransactionStatus.SUCCESSFUL, true))
                .thenReturn(Collections.emptyList());  // No active issue transaction

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.returnBooks(cardId, bookId);
        });

        assertEquals("Book with ID " + bookId + " was not issued to card ID " + cardId + ". Cannot return.", exception.getMessage());
    }

    @Test
    void testReturnBook_BookAlreadyReturned() {
        // Arrange
        int cardId = 1;
        int bookId = 100;

        // Create a mock card and book object
        Card card = new Card();
        card.setId(cardId);
        card.setCardStatus(CardStatus.ACTIVATED);

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);  // The book is available

        // Create a mock transaction object for a return operation (book already returned)
        Transaction previousReturnTransaction = new Transaction();
        previousReturnTransaction.setCard(card);
        previousReturnTransaction.setBook(book);
        previousReturnTransaction.setIsIssueOperation(false);  // Marked as return
        previousReturnTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        previousReturnTransaction.setTransactionDate(new Date());

        // Mock the behavior of repositories
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(transactionRepository.findByCard_Book(cardId, bookId, TransactionStatus.SUCCESSFUL, true))
                .thenReturn(Collections.singletonList(previousReturnTransaction));  // Simulate that book has already been returned

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.returnBooks(cardId, bookId);
        });

        assertEquals("Book with ID " + bookId + " has already been returned. Cannot return again.", exception.getMessage());
    }

}
