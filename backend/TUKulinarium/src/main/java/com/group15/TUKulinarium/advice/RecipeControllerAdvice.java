package com.group15.TUKulinarium.advice;

import com.group15.TUKulinarium.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;

@RestControllerAdvice
public class RecipeControllerAdvice {
    @ExceptionHandler(value = ImageWriterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleImageUploadException(ImageWriterException iue, WebRequest wr) {
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                iue.getMessage(),
                wr.getDescription(false)
        );
    }
    @ExceptionHandler(value = CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleCategoryNotFoundException(CategoryNotFoundException cnf, WebRequest wr) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                cnf.getMessage(),
                wr.getDescription(false)
        );
    }
    @ExceptionHandler(value = RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleRecipeNotFoundException(RecipeNotFoundException rnf, WebRequest wr) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                rnf.getMessage(),
                wr.getDescription(false)
        );
    }
    @ExceptionHandler(value = NotValidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleNotValidDataException(NotValidDataException nvd, WebRequest wr) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                nvd.getMessage(),
                wr.getDescription(false)
        );
    }
    @ExceptionHandler(value = NoPermissionsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleNotValidDataException(NoPermissionsException npd, WebRequest wr) {
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                npd.getMessage(),
                wr.getDescription(false)
        );
    }
}
