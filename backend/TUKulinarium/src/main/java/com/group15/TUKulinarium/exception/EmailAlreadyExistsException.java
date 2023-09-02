package com.group15.TUKulinarium.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistsException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EmailAlreadyExistsException(String newEmail) {
        super(String.format("User with email: %s, already exists", newEmail));
    }
}
