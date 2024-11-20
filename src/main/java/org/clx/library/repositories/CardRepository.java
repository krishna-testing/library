package org.clx.library.repositories;

import jakarta.transaction.Transactional;
import org.clx.library.model.Card;
import org.clx.library.model.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,Integer> {


    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.cardStatus = :status WHERE c.student.id = :studentId")
    void deactivateCard(@Param("studentId") int studentId, @Param("status") CardStatus status);



}
