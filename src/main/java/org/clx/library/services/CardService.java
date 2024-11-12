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
    CardRepository cardRepository;


    public Card createCard(Student student){
        Card card =new Card();
        student.setCard(card);
        card.setStudent(student);
        cardRepository.save(card);
        return card;
    }
    public void deactivate(int student_id){
        cardRepository.deactivateCard(student_id, CardStatus.DEACTIVATED.toString());

    }
}