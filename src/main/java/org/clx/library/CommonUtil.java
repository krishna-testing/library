package org.clx.library;

import org.clx.library.payload.ApiResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class CommonUtil {

    private CommonUtil() {
    }

    public static ResponseEntity<?> createBuildResponse(HttpStatusCode status, String message, Object object){
        ApiResponse response = new ApiResponse();
        response.setStatus(status.value());
        response.setMessage(message);
        response.setData(object);
        return response.create();
    }
}
