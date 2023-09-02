package com.group15.TUKulinarium.payload.response;

import com.group15.TUKulinarium.models.User;

public class SimpleUserResponse {
    private Long id;

    private String name;

    private String username;

    private String email;

    private SimpleImageResponse image;

    public SimpleUserResponse() {}

    public SimpleUserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.image = new SimpleImageResponse(user.getImage());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SimpleImageResponse getImage() {
        return image;
    }

    public void setImage(SimpleImageResponse image) {
        this.image = image;
    }

}
