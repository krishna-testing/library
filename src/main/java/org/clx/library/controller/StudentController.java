package org.clx.library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.model.Student;
import org.clx.library.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    @PostMapping("/createStudent")
    public ResponseEntity<String> createStudent(@RequestBody Student student){
        studentService.createStudent(student);
        log.info("student created successfully");
        return new ResponseEntity<>("Student Successfully added to the system", HttpStatus.CREATED);

    }

    @PutMapping("/updateStudent")
    public ResponseEntity<String> updateStudent(@RequestBody Student student){
        studentService.updateStudent(student);
        log.info("student updated successfully");
        return new ResponseEntity<>("Student updated",HttpStatus.OK);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id")int id){
        studentService.deleteStudent(id);
        log.info("student deleted successfully");
        return new ResponseEntity<>("student successfully deleted!!",HttpStatus.OK);
    }

    @GetMapping("/getStudentById")
    public ResponseEntity<Student> getStudentById(@RequestParam("id") int studentId) {
        Student student = studentService.getStudentById(studentId);
        log.info("student get with studentId : {}",student.getId());
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

}