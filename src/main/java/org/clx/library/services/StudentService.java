package org.clx.library.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.exception.StudentNotFoundException;
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

    public void createStudent(Student student) {
        studentRepository.save(student);
        Card card = cardService.createCard(student);
        log.info("Card created for student with ID: {} and card ID: {}", student.getId(), card.getId());
    }

    public void updateStudent(Student student) {
        studentRepository.updateStudentDetails(student);
        log.info("Successfully updated student with ID: {}", student.getId());
    }

    public void deleteStudent(int id) {
        cardService.deactivate(id);
        log.info("Card successfully deactivated for student ID: {}", id);
        studentRepository.deleteCustom(id);
        log.info("Successfully deleted student with ID: {}", id);
    }

    public Student getStudentById(int studentId) {
        log.info("Fetching student with ID: {}", studentId);
        return studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("Student with ID: {} not found", studentId);
                    return new StudentNotFoundException("Student with ID " + studentId + " not found");
                });
    }
}
