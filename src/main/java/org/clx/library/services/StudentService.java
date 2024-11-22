package org.clx.library.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.dto.StudentDto;
import org.clx.library.dto.StudentRequest;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.model.Card;
import org.clx.library.model.Student;
import org.clx.library.repositories.StudentRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    private final CardService cardService;

    private String studentName ="student";

    public void createStudent(StudentRequest studentRequest) {
        Student student = studentRequest.studentRequestToStudent(studentRequest);
        studentRepository.save(student);
        Card card = cardService.createCard(student);
        log.info("Card created for student with ID: {} and card ID: {}", student.getId(), card.getId());
    }

    public void updateStudent(StudentRequest studentRequest,int studentId) {
        Student student1 = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentName, "id", studentId));
        Student student = studentRequest.studentRequestToStudent();
        student.setId(studentId);
        studentRepository.updateStudentDetails(student,studentId);
        log.info("Successfully updated student with ID: {}", student1.getId());
    }

    public void deleteStudent(int id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(studentName, "id", id));
        cardService.deactivate(student.getCard().getId());
        log.info("Card successfully deactivated for student ID: {}", id);
        studentRepository.deleteCustom(id);
        log.info("Successfully deleted student with ID: {}", id);
    }

    public StudentDto getStudentById(int studentId) {
        log.info("Fetching student with ID: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("Student with ID: {} not found", studentId);
                    return new ResourceNotFoundException("student", "id", studentId);
                });
        StudentDto studentDto = new StudentDto();
        return studentDto.studentToStudentDto(student);
    }
}
