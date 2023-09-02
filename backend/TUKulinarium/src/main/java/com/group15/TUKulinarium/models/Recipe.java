package com.group15.TUKulinarium.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipes_cooking_time")
    private Integer cookingTime;

    @Column(name = "recipes_name")
    private String name;

    @Column(name = "recipes_created_on")
    private Instant createdOn;

    @Column(name = "recipes_instructions")
    private String instructions;

    @Column(name = "recipes_ingredients")
    private String ingredients;

    @Column(name = "recipes_approved")
    private boolean approved;

    @JsonManagedReference
    @OneToOne()
    @JoinTable(
            name = "user_recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private User user = new User();

    @OneToMany()
    @JoinTable(
            name = "recipe_images",
            joinColumns = @JoinColumn(name="recipe_id"),
            inverseJoinColumns = @JoinColumn(name="image_id"))
    private List<Image> images = new LinkedList<>();

    @JsonManagedReference
    @OneToMany()
    @JoinTable(
            name = "recipe_comment",
            joinColumns = @JoinColumn(name="recipe_id"),
            inverseJoinColumns = @JoinColumn(name="comment_id"))
    private List<Comment> comments = new LinkedList<>();

    @JsonManagedReference
    @ManyToOne()
    @JoinTable(
            name = "recipe_categories",
            joinColumns = @JoinColumn(name="recipe_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private Category category = new Category();

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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved() {
        this.approved = true;
    }

    public void setNotApproved(){
        this.approved = false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
