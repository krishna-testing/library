package org.clx.library.controller;

import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private static final String ISSUE_BOOK_URL = "/issueBook";

    @Test
    void testIssueBook_Success() throws Exception {
        // Arrange
        int cardId = 1;
        int bookId = 100;
        String transactionId = "TXN123456";

        // Mock the service method
        Mockito.when(transactionService.issueBooks(cardId, bookId)).thenReturn(transactionId);

        // Act & Assert
        mockMvc.perform(post(ISSUE_BOOK_URL)
                        .param("cardId", String.valueOf(cardId))
                        .param("bookId", String.valueOf(bookId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Your Transaction was successful. Here is your Txn ID: " + transactionId))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testIssueBook_Failure() throws Exception {
        // Arrange
        int cardId = 1;
        int bookId = 100;
        String errorMessage = "Book not found with id : " + bookId;

        // Mock the service to throw an exception
        Mockito.doThrow(new ResourceNotFoundException("Book", "id", bookId))
                .when(transactionService).issueBooks(cardId, bookId);

        // Act & Assert
        mockMvc.perform(post(ISSUE_BOOK_URL)
                        .param("cardId", String.valueOf(cardId))
                        .param("bookId", String.valueOf(bookId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("failed"))
                .andExpect(jsonPath("$.data").value(errorMessage));
    }

}