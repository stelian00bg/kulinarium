package com.group15.TUKulinarium.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ImageWriterException extends IOException {
    private static final long serialVersionUID = 1L;

    public ImageWriterException(String message) {
        super(message);
    }
}
