package com.group15.TUKulinarium.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ImageDeleterException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ImageDeleterException(String message) {
        super(message);
    }
}