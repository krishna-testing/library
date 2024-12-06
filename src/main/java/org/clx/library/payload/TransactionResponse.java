package org.clx.library.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.clx.library.model.TransactionStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private String transactionId;      // Unique ID of the transaction
    private int cardId;                // Card ID associated with the transaction
    private int bookId;                // Book ID involved in the transaction
    private boolean isIssueOperation; // True if book issue, false if return
    private TransactionStatus transactionStatus; // Status of the transaction (e.g., SUCCESSFUL or FAILED)
    private Date transactionDate;      // Date of the transaction
    private int fineAmount;            // Fine amount if applicable, 0 otherwise
}
