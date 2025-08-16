package com.example.server;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

public class GenericSuccessResponse {
    boolean success;
    int status;
    String message;
    protected BodyBuilder builder;

    GenericSuccessResponse(boolean success, int status,String errorMessage) {
        this.status = status;
        this.success = success;
        this.message = errorMessage;
        builder = ResponseEntity.status(status);
    }

    ResponseEntity<GenericSuccessResponse> buildResponseEntity(){
        BodyBuilder builder = ResponseEntity.status( this.status);
        return builder.body(new GenericSuccessResponse(this.success, this.status, this.message));
    }

    public static ResponseEntity<GenericSuccessResponse> buildResponseEntity(int status, boolean success, String message) {
        BodyBuilder builder = ResponseEntity.status( status);
        return builder.body(new GenericSuccessResponse(success, status, message));
    }
    

}
