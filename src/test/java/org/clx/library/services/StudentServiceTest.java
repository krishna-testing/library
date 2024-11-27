package org.clx.library.services;

import org.clx.library.dto.StudentDto;
import org.clx.library.dto.StudentRequest;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.model.Card;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.clx.library.repositories.StudentRepository;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)  // Ensure Mockito is initialized
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Mock
    private CardService cardService;


    @Test
    void testCreateStudent() {
        // Arrange
        StudentRequest studentRequest = new StudentRequest("johndoe@example.com","John Doe",25,"usa");
        Student student = new Student();
        student.setId(1); // Simulate that student is saved with ID 1

        Card card = new Card();
        card.setId(101); // Simulate card creation with ID 101

        // Mocking the behavior of the repository and card service
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(cardService.createCard(any(Student.class))).thenReturn(card);

        // Act
        studentService.createStudent(studentRequest);

        // Assert
        // Verify that the studentRepository.save() method was called with the correct student object
        verify(studentRepository, times(1)).save(any(Student.class));

        // Verify that the cardService.createCard() method was called with the correct student object
        verify(cardService, times(1)).createCard(any(Student.class));
    }

    @Test
    void testUpdateStudent_Success() {
        // Arrange
        int studentId = 1;
        StudentRequest studentRequest = new StudentRequest("john.doe@example.com", "John Doe", 40, "Canada");

        // Create a mock student object (this is the student that will be fetched from the repository)
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setName("John");
        existingStudent.setEmailId("john@example.com");
        existingStudent.setAge(80);
        existingStudent.setCountry("USA");

        // Create a mock updated student object
        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setName("John Doe");
        updatedStudent.setEmailId("john.doe@example.com");
        updatedStudent.setAge(40);
        updatedStudent.setCountry("Canada");

        // Mock the behavior of studentRepository
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));

        // Using doAnswer instead of doNothing, to ensure we capture the updated student object
        doAnswer(invocation -> {
            // The invocation will pass the updated student, so capture it and assert if needed
            Student updatedArg = invocation.getArgument(0);
            assertEquals(updatedStudent.getName(), updatedArg.getName());
            assertEquals(updatedStudent.getEmailId(), updatedArg.getEmailId());
            assertEquals(updatedStudent.getAge(), updatedArg.getAge());
            assertEquals(updatedStudent.getCountry(), updatedArg.getCountry());
            return null; // No return value since we're using void method
        }).when(studentRepository).updateStudentDetails(any(Student.class), eq(studentId));

        // Act
        studentService.updateStudent(studentRequest, studentId);

        // Assert
        // Verify that findById was called once with the studentId
        verify(studentRepository, times(1)).findById(studentId);

        // Verify that updateStudentDetails was called once with the updated student and studentId
        verify(studentRepository, times(1)).updateStudentDetails(any(Student.class), eq(studentId));
    }

    @Test
    void testUpdateStudent_StudentNotFound() {
        // Arrange
        int studentId = 1;
        StudentRequest studentRequest = new StudentRequest("john.doe@example.com","John Doe",80,"USA");

        // Mock the behavior of studentRepository
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            studentService.updateStudent(studentRequest, studentId);
        });

        // Assert
        assertEquals("student not found with id : " + studentId, exception.getMessage());
    }

    @Test
    void testDeleteStudent_Success() {
        // Arrange
        int studentId = 1;

        // Create a mock student object
        Student student = new Student();
        student.setId(studentId);
        Card studentCard = new Card();
        studentCard.setId(100);  // Mock card ID for the student
        student.setCard(studentCard);

        // Mock the behavior of the repository
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        // Mock the card deactivation and deletion behavior
        doNothing().when(cardService).deactivate(studentCard.getId());
        doNothing().when(studentRepository).deleteCustom(studentId);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        // Verify that the studentRepository methods were called
        verify(studentRepository, times(1)).findById(studentId);
        verify(cardService, times(1)).deactivate(studentCard.getId());
        verify(studentRepository, times(1)).deleteCustom(studentId);
    }

    @Test
    void testDeleteStudent_StudentNotFound() {
        // Arrange
        int studentId = 1;

        // Mock the behavior of the repository when student is not found
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            studentService.deleteStudent(studentId);
        });

        // Assert the exception message
        assertEquals("student not found with id : " + studentId, exception.getMessage());
    }

    @Test
    void testGetStudentById_Success() {
        // Arrange
        int studentId = 1;

        // Create a mock student object
        Student student = new Student();
        student.setId(studentId);
        student.setName("John Doe");
        student.setEmailId("john.doe@example.com");
        student.setAge(22);
        student.setCountry("USA");

        // Create a mock Card object and associate it with the student
        Card card = new Card();
        card.setId(100);  // Set the card ID (this may not be necessary, depending on your use case)
        student.setCard(card);

        // Mock the behavior of the repository to return the student with card
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        // Act
        StudentDto studentDto = studentService.getStudentById(studentId);

        // Assert
        assertNotNull(studentDto);  // Assert that the result is not null
        assertEquals(studentId, studentDto.getId());  // Assert that the studentId matches
        assertEquals("John Doe", studentDto.getName());  // Assert that the student name is correct
        assertEquals("john.doe@example.com", studentDto.getEmailId());  // Assert the email ID
        assertEquals(22, studentDto.getAge());  // Assert the age
        assertEquals("USA", studentDto.getCountry());  // Assert the country

        // Verify that the findById method was called once with the correct studentId
        verify(studentRepository, times(1)).findById(studentId);
    }


    @Test
    void testGetStudentById_StudentNotFound() {
        // Arrange
        int studentId = 1;

        // Mock the behavior of the repository to return empty for studentId
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            studentService.getStudentById(studentId);
        });

        // Assert the exception message
        assertEquals("student not found with id : " + studentId, exception.getMessage());
    }

}
