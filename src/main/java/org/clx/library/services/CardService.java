package org.clx.library.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.model.Card;
import org.clx.library.model.CardStatus;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CardService {


    private final CardRepository cardRepository;


    public Card createCard(Student student) {
        Card card = new Card();
        student.setCard(card);
        card.setStudent(student);
        cardRepository.save(card);
        log.info("Card successfully created for student with ID: {}", student.getId());
        return card;
    }

    public void deactivate(int studentId) {
        cardRepository.deactivateCard(studentId, CardStatus.DEACTIVATED);
        log.info("Card successfully deactivated for student with ID: {}", studentId);
    }
}