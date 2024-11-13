package org.clx.library.services;

import org.clx.library.model.Card;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Student student = new Student("John Doe","john@example.com");
        Card card = new Card(student, "ACTIVE");

        // When
        cardService.createCard(student);

        // Then
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testDeactivateCard() {
        // Given
        int cardId = 1;
        Card card = new Card(null, "ACTIVE");
        when(cardRepository.findById(cardId)).thenReturn(java.util.Optional.of(card));

        // When
        cardService.deactivate(cardId);

        // Then
        assertEquals("INACTIVE", card.getCardStatus());
        verify(cardRepository, times(1)).save(card);
    }

//    @Test
//    void testGetCardByStudentId() {
//        // Given
//        int studentId = 1;
//        Card card = new Card(null, "ACTIVE");
//        when(cardRepository.findByStudentId(studentId)).thenReturn(card);
//
//        // When
//        Card result = cardService.getCardByStudentId(studentId);
//
//        // Then
//        assertEquals(card, result);
//        verify(cardRepository, times(1)).findByStudentId(studentId);
//    }
}
