package com.io.realworld.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Error {
    DUPLICATE_USER("duplicate user", HttpStatus.CONFLICT),
    SIGNUP_NULL_DATA("request body include null",HttpStatus.BAD_REQUEST),
    EMAIL_NULL_OR_INVALID("email is blank or invalid check plz",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("user not found check your info",HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus status;

    Error(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
