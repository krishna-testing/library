package org.clx.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.dto.StudentDto;
import org.clx.library.dto.StudentRequest;
import org.clx.library.exception.ResourceNotFoundException;
import org.clx.library.payload.ApiResponse;
import org.clx.library.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    private static final String MESSAGE = "failed";


    @PostMapping("/createStudent")
    public ResponseEntity<ApiResponse> createStudent(@Valid @RequestBody StudentRequest studentRequest){
        if (studentRequest.getAge()==null){
            ApiResponse response = new ApiResponse(HttpStatus.NOT_ACCEPTABLE, MESSAGE, "please enter age");
            return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            studentService.createStudent(studentRequest);
            log.info("student created successfully");
            ApiResponse response = new ApiResponse(HttpStatus.CREATED, "Student Successfully added to the system", null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception e){
            ApiResponse response = new ApiResponse(HttpStatus.NOT_ACCEPTABLE, MESSAGE, e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);
        }


    }

    @PutMapping("/updateStudent/{studentId}")
    public ResponseEntity<ApiResponse> updateStudent(@Valid @RequestBody StudentRequest studentRequest, @PathVariable int studentId){
        try{
            studentService.updateStudent(studentRequest,studentId);
            log.info("student updated successfully");
            ApiResponse response = new ApiResponse(HttpStatus.OK, "Student updated successfully", null);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, MESSAGE, e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable("id")int id){
        try {
            studentService.deleteStudent(id);
            log.info("student deleted successfully");
            ApiResponse response = new ApiResponse(HttpStatus.OK, "student successfully deleted!!", null);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND, MESSAGE, e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") int studentId) {
        StudentDto studentDto = studentService.getStudentById(studentId);
        log.info("student get with studentId : {}",studentDto.getId());
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }

}