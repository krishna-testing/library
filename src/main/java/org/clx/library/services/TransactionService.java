package org.clx.library.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.dto.TransactionRequest;
import org.clx.library.exception.*;
import org.clx.library.model.*;
import org.clx.library.payload.TransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final WebClient webClient;

    @Value("${books.max_allowed}")
    private int maxAllowedBooks;

    @Value("${books.max_allowed_days}")
    private int maxDaysAllowed;

    @Value("${books.fine.per_day}")
    private int finePerDay;

    public String issueBooks(int cardId, int bookId) {
        log.info("Attempting to issue book with ID: {} to card ID: {}", bookId, cardId);

        // Fetch the book
        Book book = webClient.get()
                .uri("/books/{bookId}", bookId)
                .retrieve()
                .bodyToMono(Book.class)
                .block();

        if (book == null || !book.getAvailable()) {
            log.warn("Book with ID: {} is unavailable or not found!", bookId);
            throw new ResourceNotFoundException("Book", "id", bookId);
        }

        // Fetch the card
        Card card = webClient.get()
                .uri("/cards/{cardId}", cardId)
                .retrieve()
                .bodyToMono(Card.class)
                .block();

        if (card == null || card.getCardStatus() == CardStatus.DEACTIVATED) {
            log.warn("Card with ID: {} is invalid or deactivated.", cardId);
            throw new ResourceNotFoundException("Card", "id", cardId);
        }

        // Check max books limit
        if (card.getBooks().size() >= maxAllowedBooks) {
            log.warn("Card with ID: {} has reached the maximum allowed books.", cardId);
            throw new MaxAllowedBooksException("Book limit reached for this card!!");
        }

        // Create transaction request
        TransactionRequest transactionRequest = new TransactionRequest(cardId, bookId, true, 0);

        // Save the transaction
        TransactionResponse transactionResponse = webClient.post()
                .uri("/transactions")
                .bodyValue(transactionRequest)
                .retrieve()
                .bodyToMono(TransactionResponse.class)
                .block();

        log.info("Book issued successfully. Transaction ID: {}", transactionResponse.getTransactionId());
        return transactionResponse.getTransactionId();
    }

    public String returnBooks(int cardId, int bookId) {
        log.info("Attempting to return book with ID: {} for card ID: {}", bookId, cardId);

        // Fetch the book
        Book book = webClient.get()
                .uri("/books/{bookId}", bookId)
                .retrieve()
                .bodyToMono(Book.class)
                .block();

        if (book == null || book.getAvailable()) {
            log.warn("Book with ID: {} is not issued!", bookId);
            throw new IllegalArgumentException("Book with ID " + bookId + " is not currently issued.");
        }

        // Fetch the card
        Card card = webClient.get()
                .uri("/cards/{cardId}", cardId)
                .retrieve()
                .bodyToMono(Card.class)
                .block();

        if (card == null || card.getCardStatus() == CardStatus.DEACTIVATED) {
            log.warn("Card with ID: {} is invalid or deactivated.", cardId);
            throw new ResourceNotFoundException("Card", "id", cardId);
        }

        // Fetch the latest transaction
        Transaction transaction = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/transactions")
                        .queryParam("cardId", cardId)
                        .queryParam("bookId", bookId)
                        .queryParam("isIssueOperation", true)
                        .build())
                .retrieve()
                .bodyToMono(Transaction.class)
                .block();

        if (transaction == null) {
            log.warn("No issue transaction found for book ID: {} and card ID: {}.", bookId, cardId);
            throw new IllegalArgumentException("No active issue transaction found.");
        }

        // Calculate fine for late return
        Date issueDate = transaction.getTransactionDate();
        long issueTime = Math.abs(issueDate.getTime() - System.currentTimeMillis());
        long numberOfDaysPassed = TimeUnit.DAYS.convert(issueTime, TimeUnit.MILLISECONDS);
        int fine = 0;
        if (numberOfDaysPassed > maxDaysAllowed) {
            fine = (int) (numberOfDaysPassed - maxDaysAllowed) * finePerDay;
            log.info("Late return detected. Days passed: {}, Fine: {}", numberOfDaysPassed, fine);
        }

        // Create a return transaction
        TransactionRequest returnRequest = new TransactionRequest(cardId, bookId, false, fine);
        TransactionResponse returnResponse = webClient.post()
                .uri("/transactions")
                .bodyValue(returnRequest)
                .retrieve()
                .bodyToMono(TransactionResponse.class)
                .block();

        log.info("Book returned successfully. Transaction ID: {}", returnResponse.getTransactionId());
        return returnResponse.getTransactionId();
    }
}