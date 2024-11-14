package org.clx.library.services;

import org.clx.library.model.Card;
import org.clx.library.model.CardStatus;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.clx.library.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private StudentService studentService;

    @InjectMocks
    private CardService cardService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStudent() {
        // Given
        Student student = new Student("Jane Doe", "jane@example.com");
        Card card = new Card();
        card.setId(1);
        card.setCardStatus(CardStatus.ACTIVATED);
        student.setCard(card);

        // Mock behavior of cardService.createCard
        when(cardService.createCard(student)).thenReturn(card);

        // When
        studentService.createStudent(student);

        // Then
        verify(studentRepository, times(1)).save(student);  // Verify student save
        verify(cardService, times(1)).createCard(student);  // Verify card creation

        // Assert that the student's card is correctly assigned
        assertNotNull(student.getCard());
        assertEquals(card, student.getCard());
        assertEquals(1, student.getCard().getId());
        assertEquals(CardStatus.ACTIVATED, student.getCard().getCardStatus());
    }


    @Test
    void testGetStudentById() {
        // Given
        int studentId = 1;
        Student student = new Student("Jane Doe", "jane@example.com");
        when(studentRepository.findById(studentId)).thenReturn(java.util.Optional.of(student));

        // When
        Student result = studentService.getStudentById(studentId);

        // Then
        assertEquals(student, result);
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testDeleteStudent() {
        // Given
        int studentId = 1;

        // Mocking void methods: Use doNothing() for void methods
        doNothing().when(cardService).deactivate(studentId);
        doNothing().when(studentRepository).deleteCustom(studentId);

        // When
        studentService.deleteStudent(studentId);

        // Then
        verify(cardService, times(1)).deactivate(studentId);  // Verify deactivation
        verify(studentRepository, times(1)).deleteCustom(studentId);  // Verify student deletion
    }



}
