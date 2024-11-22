package org.clx.library.controller;

import org.clx.library.model.Student;
import org.clx.library.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStudent() {
        // Given
        Student student = new Student("John Doe", "john@example.com");

        // When
        ResponseEntity<String> response = studentController.createStudent(student);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Student Successfully added to the system", response.getBody());
        verify(studentService, times(1)).createStudent(student);
    }

    @Test
    void testGetStudentById() {
        // Given
        int studentId = 1;
        Student student = new Student("John Doe", "john@example.com");
        when(studentService.getStudentById(studentId)).thenReturn(student);

        // When
        ResponseEntity<Student> response = studentController.getStudentById(studentId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void testDeleteStudent() {
        // Given
        int studentId = 1;

        // When
        ResponseEntity<String> response = studentController.deleteStudent(studentId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("student successfully deleted!!", response.getBody());
        verify(studentService, times(1)).deleteStudent(studentId);
    }
}
