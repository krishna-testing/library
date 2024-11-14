package org.clx.library.services;

import lombok.AllArgsConstructor;
import org.clx.library.model.Card;
import org.clx.library.model.CardStatus;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardService {


    private final CardRepository cardRepository;


    public Card createCard(Student student) {
        Card card = new Card();
        student.setCard(card);
        card.setStudent(student);
        cardRepository.save(card);
        return card;
    }

    public void deactivate(int studentId) {
        cardRepository.deactivateCard(studentId, CardStatus.DEACTIVATED.toString());
    }
}