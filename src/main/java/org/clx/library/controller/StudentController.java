package org.clx.library.controller;

import org.clx.library.model.Student;
import org.clx.library.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {
    @Autowired
    StudentService studentService;


    @PostMapping("/createStudent")
    public ResponseEntity<String> createStudent(@RequestBody Student student){
        studentService.createStudent(student);
        return new ResponseEntity<>("Student Successfully added to the system", HttpStatus.CREATED);

    }

    @PutMapping("/updateStudent")
    public ResponseEntity<String> updateStudent(@RequestBody Student student){
        int lines=studentService.updateStudent(student);
        return new ResponseEntity<>("Student updated",HttpStatus.OK);
    }

    @DeleteMapping("/deleteStudent")
    public ResponseEntity<String> deleteStudent(@RequestParam("id")int id){
        studentService.deleteStudent(id);
        return new ResponseEntity<>("student successfully deleted!!",HttpStatus.OK);
    }


    public ResponseEntity<Student> getStudentById(int studentId) {
        Student student=studentService.getStudentById(studentId);
        return new ResponseEntity<>(student,HttpStatus.OK);
    }
}