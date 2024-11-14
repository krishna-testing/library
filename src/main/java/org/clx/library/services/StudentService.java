package org.clx.library.services;

import org.clx.library.model.Card;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.clx.library.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    public void createStudent(Student student) {
        Card card = cardService.createCard(student);
        logger.info("Card created for student with ID: {} and card ID: {}", student.getId(), card.getId());
    }

    public int updateStudent(Student student) {
        return studentRepository.updateStudentDetails(student);
    }

    public void deleteStudent(int id) {
        cardService.deactivate(id);
        studentRepository.deleteCustom(id);
    }

    public Student getStudentById(int studentId) {
        return studentRepository.findById(studentId).get();
    }
}
