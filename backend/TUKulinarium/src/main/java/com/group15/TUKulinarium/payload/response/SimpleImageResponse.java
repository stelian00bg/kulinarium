package com.group15.TUKulinarium.payload.response;

import com.group15.TUKulinarium.models.Image;

public class SimpleImageResponse {
    private String imageLink;

    public SimpleImageResponse(Image image) {
        this.imageLink = image.getLink();
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
