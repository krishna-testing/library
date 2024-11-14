package org.clx.library.repositories;

import jakarta.transaction.Transactional;
import org.clx.library.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface CardRepository extends JpaRepository<Card,Integer> {

    @Modifying
    @Query(value = "update card c set c.card_status=:new_card_status where c.id in(select card_id from student s where s.id=:student_id)", nativeQuery = true)
    void deactivateCard(@Param("student_id") int studentId, @Param("new_card_status") String newCardStatus);


}
