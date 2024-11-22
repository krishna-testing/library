package org.clx.library.exception;

import org.clx.library.CommonUtil;
import org.clx.library.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // Extract validation errors details
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Build structured response
        ApiResponse response = new ApiResponse(
                HttpStatus.NOT_ACCEPTABLE, // Use appropriate HTTP status
                "Validation Error", // Message
                errors // Include detailed validation error in `data`
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(HttpStatus.NOT_FOUND,"failed",message));
    }

}
