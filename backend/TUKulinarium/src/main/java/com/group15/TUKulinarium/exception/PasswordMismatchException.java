package com.group15.TUKulinarium.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordMismatchException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public PasswordMismatchException() {
        super("Passwords that you provided doesn't match");
    }
}
