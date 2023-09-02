package com.group15.TUKulinarium.exception;

import com.group15.TUKulinarium.models.ERole;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RoleNotFoundException(ERole eRole) {
        super(String.format("Role: %s, is missing", eRole.name()));
    }
}
