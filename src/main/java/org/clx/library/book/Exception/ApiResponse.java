package org.clx.library.book.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private HttpStatus httpStatus;
    private T error;

//    public ApiResponse(boolean success, T data, String message) {
//        this.success = success;
//        this.data = data;
//        this.message = message;
//    }
}