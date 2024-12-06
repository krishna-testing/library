package org.clx.library.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.clx.library.model.Book;
import org.clx.library.model.Card;
import org.clx.library.model.CardStatus;
import org.clx.library.model.Transaction;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    private int id;

    private Date createdOn;

    private Date updatedOn;

    @Enumerated(value = EnumType.STRING)
    private CardStatus cardStatus;

    private List<TransactionDto> transactionDtoList;

    private List<BookDto> bookDtoList;

    public Card cardDtoToCard() {
        Card card = new Card();
        card.setId(id);
        card.setCreatedOn(createdOn);
        card.setUpdatedOn(updatedOn);
        card.setCardStatus(cardStatus);

        List<Transaction> transactionList = transactionDtoList.stream()
                .map(TransactionDto::transactionDtoToTransaction)
                .toList();
        card.setTransactions(transactionList);
        List<Book> bookList = bookDtoList.stream()
                .map(BookDto::bookDtoToBook)
                .toList();
        card.setBooks(bookList);
        return card;
    }

    public CardDto cardToCardDto(Card card) {
        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setCreatedOn(card.getCreatedOn());
        cardDto.setUpdatedOn(card.getUpdatedOn());
        cardDto.setCardStatus(card.getCardStatus());

        List<TransactionDto> list = card.getTransactions() == null ?
                Collections.emptyList() :
                card.getTransactions().stream()
                        .map(TransactionDto::transactionToTransactionDto)
                        .toList();

        cardDto.setTransactionDtoList(list);
        List<BookDto> bookDtoList1 = card.getBooks() == null ?
                Collections.emptyList() :
                card.getBooks().stream()
                        .map(BookDto::bookToBookDto)
                        .toList();
        cardDto.setBookDtoList(bookDtoList1);
        return cardDto;
    }
}
