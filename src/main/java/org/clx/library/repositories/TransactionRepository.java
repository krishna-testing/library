package org.clx.library.repositories;

import jakarta.transaction.Transactional;
import org.clx.library.model.Transaction;
import org.clx.library.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("select t from Transaction t where t.card.id=:card_id and t.book.id=:book_id and t.transactionStatus=:status and t.isIssueOperation=:isIssue")
    public List<Transaction> findByCard_Book(@Param("card_id") int cardId,
                                             @Param("book_id") int bookId,
                                             @Param("status") TransactionStatus status,
                                             @Param("isIssue") boolean isIssue);
}