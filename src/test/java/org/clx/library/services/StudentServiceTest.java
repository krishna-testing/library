package org.clx.library.services;

import org.clx.library.model.Card;
import org.clx.library.model.Student;
import org.clx.library.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

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

        // When
        studentService.createStudent(student);

        // Then
        verify(studentRepository, times(1)).save(student);
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

        // When
        studentService.deleteStudent(studentId);
        cardService.deactivate(studentId);

        // Then
        verify(studentRepository, times(1)).deleteById(studentId);
    }
}
