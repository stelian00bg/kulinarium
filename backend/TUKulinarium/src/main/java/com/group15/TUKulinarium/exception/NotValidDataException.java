package com.group15.TUKulinarium.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotValidDataException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotValidDataException(String message) {
        super(message);
    }
}
