package org.clx.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clx.library.dto.StudentDto;
import org.clx.library.dto.StudentRequest;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.services.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;


    private static final String CREATE_STUDENT_URL = "/createStudent";

    private static final String UPDATE_STUDENT_URL = "/updateStudent/{studentId}";

    private static final String DELETE_STUDENT_URL = "/deleteStudent/{id}";

    private static final String GET_STUDENT_URL = "/getStudentById/{id}";

    @Test
    void testCreateStudent_ValidRequest() throws Exception {
        // Arrange
        StudentRequest studentRequest = new StudentRequest("john@example.com", "John Doe", 25, "india");

        // Act & Assert
        mockMvc.perform(post(CREATE_STUDENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Student Successfully added to the system"))
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    void testCreateStudent_MissingAge() throws Exception {
        // Arrange
        StudentRequest invalidRequest = new StudentRequest();
        invalidRequest.setName("John Doe");
        invalidRequest.setEmailId("john.doe@example.com");
        invalidRequest.setCountry("usa");

        // Act & Assert
        mockMvc.perform(post(CREATE_STUDENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").value("failed"))
                .andExpect(jsonPath("$.data").value("please enter age"))
                .andExpect(jsonPath("$.status").value(406));
    }

    @Test
    void testCreateStudent_ExceptionHandling() throws Exception {
        // Arrange
        StudentRequest validRequest = new StudentRequest();
        validRequest.setName("John Doe");
        validRequest.setAge(20);
        validRequest.setEmailId("john.doe@example.com");
        validRequest.setCountry("usa");

        Mockito.doThrow(new RuntimeException("Database error")).when(studentService).createStudent(Mockito.any(StudentRequest.class));

        // Act & Assert
        mockMvc.perform(post(CREATE_STUDENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").value("failed"))
                .andExpect(jsonPath("$.status").value(406));
    }

    @Test
    void testUpdateStudent_ValidRequest() throws Exception{
        // Arrange
        int studentId = 1;
        StudentRequest validRequest = new StudentRequest();
        validRequest.setName("John Doe");
        validRequest.setEmailId("john.doe@example.com");
        validRequest.setAge(60);
        validRequest.setCountry("usa");

        Mockito.doNothing().when(studentService).updateStudent(Mockito.any(StudentRequest.class),Mockito.eq(studentId));

        // Act & Assert
        mockMvc.perform(put(UPDATE_STUDENT_URL,studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student updated successfully"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testUpdateStudent_ResourceNotFound() throws Exception{
        // Arrange
        int studentId = 100;
        StudentRequest validRequest = new StudentRequest();
        validRequest.setName("John Doe");
        validRequest.setEmailId("john.doe@example.com");
        validRequest.setAge(60);
        validRequest.setCountry("usa");

        Mockito.doThrow(new ResourceNotFoundException("student","id",studentId))
                .when(studentService).updateStudent(Mockito.any(StudentRequest.class),Mockito.eq(studentId));

        // Act & Assert
        mockMvc.perform(put(UPDATE_STUDENT_URL,studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("failed"))
                .andExpect(jsonPath("$.data").value("student not found with id : 100"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testDeleteStudent_Success() throws Exception{
        // Arrange
        int studentId=1;

        // Act & Assert
        mockMvc.perform(delete(DELETE_STUDENT_URL,studentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("student successfully deleted!!"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testDeleteStudent_ResourceNotFound() throws Exception{
        // Arrange
        int studentId = 100;

        Mockito.doThrow(new ResourceNotFoundException("student","id",studentId))
                .when(studentService).deleteStudent(studentId);

        //Act & Assert
        mockMvc.perform(delete(DELETE_STUDENT_URL,studentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("failed"))
                .andExpect(jsonPath("$.data").value("student not found with id : 100"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testGetStudentById_Success() throws Exception {
        // Arrange
        int studentId = 1;
        StudentDto studentDto = new StudentDto();
        studentDto.setId(studentId);
        studentDto.setName("John Doe");
        studentDto.setEmailId("john.doe@example.com");
        studentDto.setAge(25);
        studentDto.setCountry("usa");

        Mockito.when(studentService.getStudentById(studentId)).thenReturn(studentDto);

        // Act & Assert
        mockMvc.perform(get(GET_STUDENT_URL, studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.emailId").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.country").value("usa"));
    }

    @Test
    void testGetStudentById_ResourceNotFound() throws Exception {
        // Arrange
        int studentId = 100; // Non-existent ID

        Mockito.when(studentService.getStudentById(studentId))
                .thenThrow(new ResourceNotFoundException("student", "id", studentId));

        // Act & Assert
        mockMvc.perform(get(GET_STUDENT_URL, studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("failed"))
                .andExpect(jsonPath("$.data").value("student not found with id : 100"))
                .andExpect(jsonPath("$.status").value(404));
    }

}
