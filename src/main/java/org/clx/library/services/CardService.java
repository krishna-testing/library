package org.clx.library.services;

import lombok.AllArgsConstructor;
import org.clx.library.model.Card;
import org.clx.library.model.CardStatus;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);
    private final CardRepository cardRepository;

    // Create a new card for the student
    public Card createCard(Student student) {
        logger.info("Received request to create card for student with ID: {} and name: {}", student.getId(), student.getName());
        try {
            // Ensure student card is not already created
            if (student.getCard() != null) {
                logger.warn("Student with ID: {} already has an active card.", student.getId());
                throw new IllegalStateException("Student already has an active card.");
            }

            Card card = new Card();
            student.setCard(card);
            card.setStudent(student);
            Card savedCard = cardRepository.save(card);

            logger.info("Card created successfully for student with ID: {}. Card ID: {}", student.getId(), savedCard.getId());
            return savedCard;
        } catch (Exception e) {
            // Log and rethrow the exception with contextual information
            logger.error("Error occurred while creating card for student with ID: {}. Error: {}", student.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to create card for student with ID: " + student.getId(), e);
        }
    }

    // Deactivate the student's card
    public void deactivate(int studentId) {
        logger.info("Received request to deactivate card for student with ID: {}", studentId);
        try {
            // Deactivating the card
            cardRepository.deactivateCard(studentId, CardStatus.DEACTIVATED.toString());
            logger.info("Card for student with ID: {} has been deactivated successfully.", studentId);
        } catch (Exception e) {
            // Log and rethrow the exception with contextual information
            logger.error("Error occurred while deactivating card for student with ID: {}. Error: {}", studentId, e.getMessage(), e);
            throw new RuntimeException("Failed to deactivate card for student with ID: " + studentId, e);
        }
    }
}
