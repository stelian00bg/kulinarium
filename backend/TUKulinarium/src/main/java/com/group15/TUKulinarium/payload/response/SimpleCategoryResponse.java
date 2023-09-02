package com.group15.TUKulinarium.payload.response;

import com.group15.TUKulinarium.models.Category;

public class SimpleCategoryResponse {
    private Long id;

    private String name;

    private String imageLink;

    public SimpleCategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.imageLink = category.getImage().getLink();
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
