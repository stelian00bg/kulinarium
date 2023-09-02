package com.group15.TUKulinarium.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateEmailRequest {
    @NotBlank
    @Size(max = 50)
    @Email
    private String newEmail;

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String email) {
        this.newEmail = email;
    }
}
