package org.clx.library.payload;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    private int status;
    private String message;
    private Object data;

    public ResponseEntity<?> create(){
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("status",status);
        response.put("message",message);
        response.put("data",data);

        return new ResponseEntity<>(response, HttpStatus.valueOf(status));
    }
}
