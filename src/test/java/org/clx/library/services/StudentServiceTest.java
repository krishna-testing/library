package org.clx.library.services;

import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.clx.library.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private Student student;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student("Jane Doe", "jane@example.com");
    }





    @Test
    void testGetStudentById() {
        // Given
        int studentId = 1;
        when(studentRepository.findById(studentId)).thenReturn(java.util.Optional.of(student));

        // When
        Student result = studentService.getStudentById(studentId);

        // Then
        assertEquals(student, result);
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void updateStudent_shouldUpdateStudentDetails() {
        // Calling the method
        studentService.updateStudent(student);

        // Verifying interactions
        verify(studentRepository, times(1)).updateStudentDetails(student);
    }

    @Test
    void getStudentById_shouldReturnStudentIfExists() {
        int studentId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        // Calling the method
        Student result = studentService.getStudentById(studentId);

        // Assertions
        assertNotNull(result);
        assertEquals(student.getName(), result.getName());
        assertEquals(student.getEmailId(), result.getEmailId());

        // Verifying interactions
        verify(studentRepository, times(1)).findById(studentId);
    }


    @Test
    void getStudentById_shouldThrowExceptionIfStudentNotFound() {
        int studentId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Assertions
        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById(studentId));

        // Verifying interactions
        verify(studentRepository, times(1)).findById(studentId);
    }

}
