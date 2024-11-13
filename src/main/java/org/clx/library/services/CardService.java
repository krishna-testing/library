package org.clx.library.services;

import org.clx.library.model.Card;
import org.clx.library.model.CardStatus;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public Card createCard(Student student) {
        Card card = new Card();
        card.setStudent(student);
        card.setCardStatus(CardStatus.ACTIVATED); // Explicitly setting card status
        student.setCard(card);  // Establishing bi-directional relationship
        cardRepository.save(card);
        return card;
    }

    public void deactivate(int studentId) {
        cardRepository.deactivateCard(studentId, CardStatus.DEACTIVATED.toString());
    }
}
