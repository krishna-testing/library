package org.clx.library.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private int cardId; // ID of the card involved in the transaction
    private int bookId; // ID of the book involved in the transaction
    private boolean isIssueOperation; // True if issuing the book, false if returning
    private int fineAmount; // Fine amount applicable, 0 for issuing transactions
}
