package org.clx.library.services;

import lombok.RequiredArgsConstructor;
import org.clx.library.exception.StudentNotFoundException;
import org.clx.library.model.Card;
import org.clx.library.model.Student;
import org.clx.library.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    private final CardService cardService;

    public void createStudent(Student student) {
        Card card = cardService.createCard(student);
        logger.info("Card created for student with ID: {} and card ID: {}", student.getId(), card.getId());
    }

    public void updateStudent(Student student) {
        studentRepository.updateStudentDetails(student);
    }

    public void deleteStudent(int id) {
        cardService.deactivate(id);
        studentRepository.deleteCustom(id);
    }

    public Student getStudentById(int studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student with ID " + studentId + " not found"));
    }
}
