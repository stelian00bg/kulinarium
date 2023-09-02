package com.group15.TUKulinarium.payload.response;

import com.group15.TUKulinarium.models.*;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class SimpleRecipeResponse {
    private Long id;

    private Integer cookingTime;

    private String name;

    private Instant createdOn;

    private String instructions;

    private String ingredients;

    private SimpleUserResponse user = new SimpleUserResponse();

    private List<SimpleImageResponse> images = new LinkedList<>();

    private List<SimpleCommentResponse> comments = new LinkedList<>();

    private SimpleCategoryResponse category;

    public SimpleRecipeResponse(Recipe recipe) {
        this.id = recipe.getId();
        this.cookingTime = recipe.getCookingTime();
        this.name = recipe.getName();
        this.createdOn = recipe.getCreatedOn();
        this.instructions = recipe.getInstructions();
        this.ingredients = recipe.getIngredients();
        this.user = new SimpleUserResponse(recipe.getUser());
        for (var image: recipe.getImages()) {
            this.images.add(new SimpleImageResponse(image));
        }
        for (var comment: recipe.getComments()){
            this.comments.add(new SimpleCommentResponse(comment));
        }
        this.category = new SimpleCategoryResponse(recipe.getCategory());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public SimpleUserResponse getUser() {
        return user;
    }

    public void setUser(SimpleUserResponse user) {
        this.user = user;
    }

    public List<SimpleImageResponse> getImages() {
        return images;
    }

    public void setImages(List<SimpleImageResponse> images) {
        this.images = images;
    }

    public List<SimpleCommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<SimpleCommentResponse> comments) {
        this.comments = comments;
    }

    public SimpleCategoryResponse getCategory() {
        return category;
    }

    public void setCategory(SimpleCategoryResponse category) {
        this.category = category;
    }
}
