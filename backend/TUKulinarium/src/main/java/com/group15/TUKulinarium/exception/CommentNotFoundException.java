package com.group15.TUKulinarium.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CommentNotFoundException(String message) {
        super(message);
    }
}
