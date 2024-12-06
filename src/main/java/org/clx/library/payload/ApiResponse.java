package org.clx.library.payload;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    private int status;
    private String message;
    private Object data;

    public ApiResponse(HttpStatus httpStatus, String message, Object data) {
        this.status = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    public ResponseEntity<ApiResponse> create() {

        return new ResponseEntity<>(this, HttpStatus.valueOf(status));
    }
}
