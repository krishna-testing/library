package org.clx.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.clx.library.model.Book;
import org.clx.library.model.Transaction;
import org.clx.library.model.TransactionStatus;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private int id;

    private String transactionId;


    private int fineAmount;

    @JsonIgnore
    private BookDto bookDto;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isIssueOperation;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private Date transactionDate;

    public Transaction transactionDtoToTransaction(){
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setTransactionId(transactionId);
        transaction.setFineAmount(fineAmount);
//        transaction.setBook(bookDto.bookDtoToBook());
        transaction.setIsIssueOperation(isIssueOperation);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setTransactionDate(transactionDate);
        return transaction;
    }

    public static TransactionDto transactionToTransactionDto(Transaction transaction){
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setTransactionId(transaction.getTransactionId());
        transactionDto.setFineAmount(transaction.getFineAmount());
//        Book book = transaction.getBook();
//        transactionDto.setBookDto(BookDto.bookToBookDto(book));
        transactionDto.setIsIssueOperation(transaction.getIsIssueOperation());
        transactionDto.setTransactionStatus(transaction.getTransactionStatus());
        transactionDto.setTransactionDate(transaction.getTransactionDate());
        return transactionDto;
    }
}
