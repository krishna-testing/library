package org.clx.library.exception;

public class StudentNotFoundException extends RuntimeException{

    public StudentNotFoundException(String message) {
        super(message);
    }
}
