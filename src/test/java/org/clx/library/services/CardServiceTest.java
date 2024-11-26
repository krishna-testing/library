package org.clx.library.services;

import org.clx.library.exception.ResourceNotFoundException;
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
    void testCreateCard_Success() {
        // Arrange
        Student student = new Student();
        student.setId(1); // Assign a mock student ID
        student.setName("John Doe");

        Card mockCard = new Card();
        mockCard.setId(1); // Mock card ID
        mockCard.setStudent(student);

        // Mock the repository to return the saved card
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card cardToSave = invocation.getArgument(0); // Capture the card being saved
            cardToSave.setId(1); // Simulate ID generation
            return cardToSave;
        });

        // Act
        Card createdCard = cardService.createCard(student);

        // Assert
        assertEquals(mockCard.getId(), createdCard.getId(), "Card ID should match");
        verify(cardRepository).save(any(Card.class)); // Ensure the repository's save method was called
        assertEquals(student, createdCard.getStudent(), "The student should be associated with the card");
    }


    @Test
    void testDeactivate_ResourceNotFound() {
        // Arrange
        int studentId = 1;
        when(cardRepository.findById(studentId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            cardService.deactivate(studentId);
        });

        verify(cardRepository).findById(studentId); // Ensure findById was called
    }


}
