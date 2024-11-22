package org.clx.library.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.clx.library.model.Card;
import org.clx.library.model.CardStatus;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    private int id;

    private Date createdOn;

    private Date updatedOn;

    @Enumerated(value=EnumType.STRING)
    private CardStatus cardStatus;

    private List<TransactionDto> transactionDtoList;

    private List<BookDto> bookDtoList;

    public Card cardDtoToCard(){
        Card card= new Card();
        card.setId(id);
        card.setCreatedOn(createdOn);
        card.setUpdatedOn(updatedOn);
        card.setCardStatus(cardStatus);
        card.setTransactions(transactionDtoList);
        card.setBooks(bookDtoList);
        return card;
    }

    public CardDto cardToCardDto(Card card){
        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setCreatedOn(card.getCreatedOn());
        cardDto.setUpdatedOn(card.getUpdatedOn());
        cardDto.setCardStatus(card.getCardStatus());
        cardDto.setTransactionDtoList(card.getTransactions());
        cardDto.setBookDtoList(card.getBooks());
        return cardDto;
    }
}
