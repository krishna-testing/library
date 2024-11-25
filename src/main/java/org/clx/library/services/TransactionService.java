package org.clx.library.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.exception.*;
import org.clx.library.model.*;
import org.clx.library.repositories.BookRepository;
import org.clx.library.repositories.CardRepository;
import org.clx.library.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final BookRepository bookRepository;

    private final CardRepository cardRepository;
    @Value("${books.max_allowed}")
    int maxAllowedBooks;
    @Value("${books.max_allowed_days}")
    int maxDaysAllowed;
    @Value("${books.fine.per_day}")
    int finePerDay;


    public String issueBooks(int cardId,int bookId) {
        log.info("Attempting to issue book with ID: {} to card ID: {}", bookId, cardId);
        // Use findById() and check if the book exists
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            log.warn("Book with ID: {} not found!", bookId);
            throw new ResourceNotFoundException("Book","id",bookId);
        }

        Book book = optionalBook.get(); // Now it's safe to use book

        // Check if the book is available
        if (!book.getAvailable()) {  // Use getAvailable() instead of isAvailable()
            log.warn("Book with ID: {} is unavailable!", bookId);
            throw new ResourceNotFoundException("Book","id",bookId);
        }

        // Check if the card exists and is valid
        Card card=cardRepository.findById(cardId)
                .orElseThrow(()-> {
                    log.warn("Card with ID: {} not found!", cardId);
                    return new ResourceNotFoundException("Card","id",cardId);
                });

        if (card==null||card.getCardStatus()== CardStatus.DEACTIVATED){
            log.warn("Card with ID: {} is deactivated.", cardId);
            throw new ResourceNotFoundException("Card","id",cardId);
        }

        // Check if the card has reached its book limit
        if (card.getBooks().size()>maxAllowedBooks){
            log.warn("Card with ID: {} has reached the maximum allowed books.", cardId);
            throw new MaxAllowedBooksException("Book limit reached for this card!!");
        }

        // Issue the book
        log.info("Issuing book with ID: {} to card ID: {}", bookId, cardId);
        book.setAvailable(false);
        book.setCard(card);
        List<Book> books=card.getBooks();
        books.add(book);
        card.setBooks(books);
        bookRepository.updateBook(book);
        log.info("Book with ID: {} has been successfully updated.", bookId);

        Transaction transaction=new Transaction();
        transaction.setCard(card);
        transaction.setBook(book);
        transaction.setIsIssueOperation(true);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(transaction);
        log.info("Transaction successful. Transaction ID: {}", transaction.getTransactionId());

        return transaction.getTransactionId();
    }

    public String returnBooks(int cardId, int bookId) {
        log.info("Attempting to return book with ID: {} for card ID: {}", bookId, cardId);

        // Validate if the card and book exist
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        // Fetch active issue transactions for the given card and book
        List<Transaction> transactions = transactionRepository.findByCard_Book(cardId, bookId, TransactionStatus.SUCCESSFUL, true);

        // Check if no valid issue transactions exist
        if (transactions == null || transactions.isEmpty()) {
            log.error("No active issue transaction found for book ID: {} and card ID: {}. Cannot proceed with return.", bookId, cardId);
            throw new IllegalArgumentException("Book with ID " + bookId + " was not issued to card ID " + cardId + ". Cannot return.");
        }

        // Get the last transaction
        Transaction lastTransaction = transactions.get(transactions.size() - 1);

        // Ensure the last transaction is an issue operation
        if (!lastTransaction.getIsIssueOperation()) {
            log.error("The last transaction for book ID: {} and card ID: {} is not an issue operation. Cannot return.", bookId, cardId);
            throw new IllegalArgumentException("Book with ID " + bookId + " has already been returned. Cannot return again.");
        }

        // Calculate fine if the book is returned late
        Date issueDate = lastTransaction.getTransactionDate();
        long issueTime = Math.abs(issueDate.getTime() - System.currentTimeMillis());
        long numberOfDaysPassed = TimeUnit.DAYS.convert(issueTime, TimeUnit.MILLISECONDS);
        int fine = 0;
        if (numberOfDaysPassed > maxDaysAllowed) {
            fine = (int) (numberOfDaysPassed - maxDaysAllowed) * finePerDay;
            log.info("Late return detected. Days passed: {}, Fine: {}", numberOfDaysPassed, fine);
        }

        // Update the book's availability
        book.setCard(null);
        book.setAvailable(true);
        bookRepository.save(book);
        log.info("Book with ID: {} has been successfully returned and updated.", bookId);

        // Create a new return transaction
        Transaction newTransaction = new Transaction();
        newTransaction.setBook(book);
        newTransaction.setCard(card);
        newTransaction.setFineAmount(fine);
        newTransaction.setIsIssueOperation(false);
        newTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        newTransaction.setTransactionDate(new Date());
        transactionRepository.save(newTransaction);

        log.info("Return transaction successful. Transaction ID: {}", newTransaction.getTransactionId());
        return newTransaction.getTransactionId();
    }


}