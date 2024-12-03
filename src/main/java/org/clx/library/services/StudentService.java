package org.clx.library.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.dto.StudentDto;
import org.clx.library.dto.StudentRequest;
import org.clx.library.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final WebClient webClient;



    public StudentRequest createStudent(StudentRequest studentRequest) {
        logger.info("sending request to create student");
        return webClient.post()
                .uri("/api/student/createStudent")
                .body(Mono.just(studentRequest), StudentRequest.class)
                .retrieve()
                .bodyToMono(StudentRequest.class)
                .doOnSuccess(response -> logger.info("Student created successfully"))
                .block();

    }
    public StudentRequest updateStudent(StudentRequest studentRequest, Integer authorId) throws ResourceNotFoundException {
        logger.info("Sending request to update author with ID: {}", authorId);
        return webClient.put()
                .uri("/api/student/updateStudent/{studentId}", authorId)
                .body(Mono.just(studentRequest), StudentRequest.class)
                .retrieve()
                .bodyToMono(StudentRequest.class)
                .doOnSuccess(response -> logger.info("Student updated successfully: {}", response.getName()))
                .block();
    }

    public void deleteStudent(int studentId) {
        logger.info("Sending request to delete student with ID: {}", studentId);
        webClient.delete()
                .uri("/api/student/deleteStudent/{id}", studentId)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> logger.info("Student deleted successfully"))
                .block();
    }

    public StudentDto getStudentById(Integer studentId) throws ResourceNotFoundException {
        logger.info("Fetching student with ID: {}", studentId);
        return webClient.get()
                .uri("/api/student/getStudentById/{id}", studentId)
                .retrieve()
                .bodyToMono(StudentDto.class)
                .doOnError(e -> logger.error("Error fetching student: {}", e.getMessage()))
                .block();
    }
}
