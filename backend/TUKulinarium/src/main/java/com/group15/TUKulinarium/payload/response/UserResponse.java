package com.group15.TUKulinarium.payload.response;

import com.group15.TUKulinarium.models.Image;
import com.group15.TUKulinarium.models.Recipe;
import com.group15.TUKulinarium.models.Role;
import com.group15.TUKulinarium.models.User;
import com.group15.TUKulinarium.repository.UserRepository;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class UserResponse {
    private String name;
    private String username;
    private String email;
    private Image image;
    private List<Recipe> recipes = new LinkedList<>();
    private Set<Role> roles = new HashSet<>();

    public UserResponse(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.image = user.getImage();
        this.recipes = user.getRecipes();
        this.roles = user.getRoles();
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
