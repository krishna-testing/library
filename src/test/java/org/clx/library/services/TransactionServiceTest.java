package org.clx.library.services;

import org.clx.library.model.*;
import org.clx.library.repositories.BookRepository;
import org.clx.library.repositories.CardRepository;
import org.clx.library.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
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
    void testIssueBook_Successful() throws Exception {
        // Mocking
        int cardId = 1;
        int bookId = 1;
        Book book = new Book();
        book.setAvailable(true);
        Card card = new Card();
        card.setCardStatus(CardStatus.ACTIVATED);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test
        String transactionId = transactionService.issueBooks(cardId, bookId);

        // Assertions
        assertNotNull(transactionId);
        assertFalse(book.getAvailable());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testIssueBook_BookUnavailable() {
        // Mocking
        int cardId = 1;
        int bookId = 1;
        Book book = new Book();
        book.setAvailable(false);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Test & Assertions
        Exception exception = assertThrows(Exception.class, () -> transactionService.issueBooks(cardId, bookId));
        assertEquals("Book is unavailable!", exception.getMessage());
    }

    @Test
    void testReturnBook_WithFine() throws Exception {
        // Mocking
        int cardId = 1;
        int bookId = 1;
        Book book = new Book();
        book.setAvailable(false);
        Card card = new Card();
        Transaction lastTransaction = new Transaction();
        lastTransaction.setTransactionDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))); // 30 days ago
        lastTransaction.setBook(book);
        lastTransaction.setCard(card);

        when(transactionRepository.findByCard_Book(cardId, bookId, TransactionStatus.SUCCESSFUL, true))
                .thenReturn(Arrays.asList(lastTransaction));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test
        String transactionId = transactionService.returnBooks(cardId, bookId);

        // Assertions
        assertNotNull(transactionId);
        assertTrue(book.getAvailable());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
