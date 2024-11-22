package org.clx.library.services;

import org.clx.library.model.Card;
import org.clx.library.model.CardStatus;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIssueCard() {
        // Given
        Student student = new Student("John Doe", "john@example.com");

        // When
        Card createdCard = cardService.createCard(student);

        // Then
        verify(cardRepository, times(1)).save(createdCard);
    }

    @Test
    void testDeactivateCard() {
        // Given
        int studentId = 1;
        Card card = new Card();
        card.setCardStatus(CardStatus.ACTIVATED); // Set initial status
        when(cardRepository.findById(studentId)).thenReturn(java.util.Optional.of(card));

        // When
        cardService.deactivate(studentId);

        // Then
        verify(cardRepository, times(1)).deactivateCard(studentId, CardStatus.DEACTIVATED);
    }

}
