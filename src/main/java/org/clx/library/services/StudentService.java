package org.clx.library.services;

import org.clx.library.model.Card;
import org.clx.library.model.Student;
import org.clx.library.repositories.CardRepository;
import org.clx.library.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    Logger logger= LoggerFactory.getLogger(StudentService.class);


    @Autowired
    StudentRepository studentRepository ;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardService cardService;

    public void createStudent (Student student){

        Card card=cardService.createCard(student);
        logger.info("The card for the student{} is created with the card details{}",student,card);


    }
    public int  updateStudent(Student student){
        return studentRepository.updateStudentDetails(student);


    }


    public void deleteStudent(int id){

        cardService.deactivate(id);
        studentRepository.deleteCustom(id);

    }

    public Student getStudentById(int studentId) {
        Student student = studentRepository.findById(studentId).get();
        return student;
    }
}