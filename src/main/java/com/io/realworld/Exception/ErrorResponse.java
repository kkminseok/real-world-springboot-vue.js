package com.io.realworld.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public ErrorResponse(Error error){
        this.status = error.getStatus().value();
        this.error = error.getStatus().name();
        this.code = error.name();
        this.message = error.getMessage();
    }
}
