package com.group15.TUKulinarium.payload.request;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class ChangeProfilePictureRequest {
    @NotNull
    private MultipartFile picture;

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
