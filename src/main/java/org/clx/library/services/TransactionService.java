package org.clx.library.services;

import lombok.RequiredArgsConstructor;
import org.clx.library.exception.BookNotFoundException;
import org.clx.library.exception.CardInvalidException;
import org.clx.library.exception.CardNotFoundException;
import org.clx.library.exception.MaxAllowedBooksException;
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


    public String issueBooks(int cardId, int bookId) throws BookNotFoundException, CardNotFoundException {

        // Use findById() and check if the book exists
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book not found!!");
        }

        Book book = optionalBook.get();

        // Check if the book is available
        if (!book.getAvailable()) {  // Use getAvailable() instead of isAvailable()
            throw new BookNotFoundException("Book is unavailable!");
        }

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card Not Found With id : " + cardId));
        if (card == null || card.getCardStatus() == CardStatus.DEACTIVATED) {
            throw new CardInvalidException("Card is invalid!!");
        }
        if (card.getBooks().size() > maxAllowedBooks) {
            throw new MaxAllowedBooksException("Book limit reached for this card!!");
        }
        book.setAvailable(false);
        book.setCard(card);
        List<Book> books = card.getBooks();
        books.add(book);
        card.setBooks(books);
        bookRepository.updateBook(book);
        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setBook(book);
        transaction.setIsIssueOperation(true);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(transaction);
        return transaction.getTransactionId();
    }

    public String returnBooks(int cardId, int bookId) throws BookNotFoundException, CardNotFoundException {
        List<Transaction> transactions = transactionRepository.findByCard_Book(cardId, bookId, TransactionStatus.SUCCESSFUL, true);
        Transaction lastIssueTransaction = transactions.get(transactions.size() - 1);
        //Last transaction that has been done ^^^^
        Date issueDate = lastIssueTransaction.getTransactionDate();
        long issueTime = Math.abs(issueDate.getTime() - System.currentTimeMillis());
        long numberOfDaysPassed = TimeUnit.DAYS.convert(issueTime, TimeUnit.MILLISECONDS);
        int fine = 0;
        if (numberOfDaysPassed > maxDaysAllowed) {
            fine = (int) Math.abs(numberOfDaysPassed - maxDaysAllowed) * finePerDay;
        }
        Card card = lastIssueTransaction.getCard();
        Book book = lastIssueTransaction.getBook();
        book.setCard(null);
        book.setAvailable(true);
        bookRepository.updateBook(book);
        Transaction newTransaction = new Transaction();
        newTransaction.setBook(book);
        newTransaction.setCard(card);
        newTransaction.setFineAmount(fine);
        newTransaction.setIsIssueOperation(false);
        newTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(newTransaction);
        return newTransaction.getTransactionId();
    }


}