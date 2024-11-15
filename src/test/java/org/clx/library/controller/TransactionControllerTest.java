package org.clx.library.controller;

import org.clx.library.exception.BookNotFoundException;
import org.clx.library.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIssueBook_Successful() throws Exception {
        int cardId = 1;
        int bookId = 1;
        String expectedTransactionId = "txn123";

        when(transactionService.issueBooks(cardId, bookId)).thenReturn(expectedTransactionId);

        ResponseEntity<String> response = transactionController.issueBook(cardId, bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Your Transaction was successful. Here is your Txn ID: txn123", response.getBody());
        verify(transactionService, times(1)).issueBooks(cardId, bookId);
    }

    @Test
    void testIssueBook_Failure() throws Exception {
        int cardId = 1;
        int bookId = 1;

        when(transactionService.issueBooks(cardId, bookId)).thenThrow(new BookNotFoundException("Book is unavailable!"));

        ResponseEntity<String> response = transactionController.issueBook(cardId, bookId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Book is unavailable!", response.getBody());
        verify(transactionService, times(1)).issueBooks(cardId, bookId);
    }

    @Test
    void testReturnBook_Successful() throws Exception {
        int cardId = 1;
        int bookId = 1;
        String expectedTransactionId = "txn456";

        when(transactionService.returnBooks(cardId, bookId)).thenReturn(expectedTransactionId);

        ResponseEntity<String> response = transactionController.returnBook(cardId, bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Your Transaction was Successful here is your Txn id:txn456", response.getBody());
        verify(transactionService, times(1)).returnBooks(cardId, bookId);
    }

    @Test
    void testReturnBook_Failure() throws Exception {
        int cardId = 1;
        int bookId = 1;

        when(transactionService.returnBooks(cardId, bookId)).thenThrow(new BookNotFoundException("Transaction failed"));

        ResponseEntity<String> response = transactionController.returnBook(cardId, bookId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Transaction failed", response.getBody());
        verify(transactionService, times(1)).returnBooks(cardId, bookId);
    }
}