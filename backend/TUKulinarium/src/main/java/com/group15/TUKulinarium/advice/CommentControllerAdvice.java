package com.group15.TUKulinarium.advice;

import com.group15.TUKulinarium.exception.CommentNotFoundException;
import com.group15.TUKulinarium.exception.NoPermissionsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class CommentControllerAdvice {
    @ExceptionHandler(value = CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleRecipeNotFoundException(CommentNotFoundException cnf, WebRequest wr) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                cnf.getMessage(),
                wr.getDescription(false)
        );
    }

    @ExceptionHandler(value = NoPermissionsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleNoPermissionsException(NoPermissionsException ex, WebRequest wr) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                wr.getDescription(false)
        );
    }
}
