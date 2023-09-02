package com.group15.TUKulinarium.controllers;

import com.group15.TUKulinarium.exception.*;
import com.group15.TUKulinarium.models.ERole;
import com.group15.TUKulinarium.models.Image;
import com.group15.TUKulinarium.models.Recipe;
import com.group15.TUKulinarium.payload.request.CreateRecipeRequest;
import com.group15.TUKulinarium.payload.response.SimpleCommentResponse;
import com.group15.TUKulinarium.payload.response.SimpleRecipeResponse;
import com.group15.TUKulinarium.repository.CategoryRepository;
import com.group15.TUKulinarium.repository.ImageRepository;
import com.group15.TUKulinarium.repository.RecipeRepository;
import com.group15.TUKulinarium.repository.UserRepository;
import com.group15.TUKulinarium.service.ImageDeleter;
import com.group15.TUKulinarium.service.ImageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageWriter imageWriter;

    @Autowired
    ImageDeleter imageDeleter;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id){
        var recipe = recipeRepository.findById(id).orElseThrow(()->
                new RecipeNotFoundException(String.format("recipe with id: %d, can't be found", id)));
        return ResponseEntity.ok(new SimpleRecipeResponse(recipe));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchRecipe(@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
                                          @RequestParam(value = "perPage", defaultValue = "5") int perPage,
                                          @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                          @RequestParam(value = "cookingTime", required = false, defaultValue = "10000000") int cookingTime,
                                          @RequestParam(value = "username", required = false, defaultValue = "") String username,
                                          @RequestParam(value = "categoryName", required = false, defaultValue = "") String categoryName){
        Pageable pageable = PageRequest.of(currentPage-1, perPage);
        Page<Recipe> recipes = recipeRepository.findPagedRecipes(pageable, name.toLowerCase(), cookingTime, username.toLowerCase(), categoryName.toLowerCase());
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException(String.format("there aren't recipes with following params: \nRecipe Name: %s\nOwner Username: %s\nCooking Time: %d\nCategory Name: %s",
                    name, username, cookingTime, categoryName));
        }
        List<SimpleRecipeResponse> recipeResponseList = new LinkedList<>();
        recipes.forEach(recipe -> recipeResponseList.add(new SimpleRecipeResponse(recipe)));
        return ResponseEntity.ok(recipeResponseList);

    }

    @GetMapping(value = "/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> recipesPendingApproval(@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
                                                    @RequestParam(value = "perPage", defaultValue = "5") int perPage) throws RecipeNotFoundException{
        Pageable pageable = PageRequest.of(currentPage-1, perPage);
        Page<Recipe> recipes = recipeRepository.findPagedPendingApproval(pageable);
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException("there aren't any recipes that needs approval");
        }
        List<SimpleRecipeResponse> recipeResponseList = new LinkedList<>();
        recipes.forEach(recipe -> recipeResponseList.add(new SimpleRecipeResponse(recipe)));
        return ResponseEntity.ok(recipeResponseList);
    }

    @GetMapping(value = "/comments")
    public ResponseEntity<?> getCommentsFromRecipe(@RequestParam(value = "recipeId") Long id){
        var recipe = recipeRepository.findById(id).orElseThrow(
                ()-> new RecipeNotFoundException(String.format("recipe with id: %d, can't be found", id))
        );
        List<SimpleCommentResponse> comments = new LinkedList<>();
        for (var comment:
                recipe.getComments()) {
            comments.add(new SimpleCommentResponse(comment));
        }
        return ResponseEntity.ok(comments);
    }

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createRecipe(@Valid @ModelAttribute CreateRecipeRequest request,
                                          Authentication auth) throws ImageWriterException {
        Recipe recipe = new Recipe();
        recipe.setNotApproved();

        var category = categoryRepository.findByName(request.getCategory()).
                orElseThrow(() -> new CategoryNotFoundException(String.format("category with name %s, can't be found", request.getCategory())));
        recipe.setCategory(category);

        recipe.setName(request.getName());

        var cookingTime = request.getCookingTime();
        if(cookingTime<0 || cookingTime>10000){
            throw new NotValidDataException(String.format("cooking time of: %d, is not valid input", cookingTime));
        }
        recipe.setCookingTime(cookingTime);

        recipe.setIngredients(request.getIngredients());

        recipe.setInstructions(request.getInstructions());

        recipe.setCreatedOn(Instant.now());


        List<Image> images = new LinkedList<>();
        if(request.getFiles() == null) {
            images.add(imageRepository.findById(1L).orElseThrow(
                    () -> new ImageNotFoundException(String.format("image with id: %d, can't be found", 1))
            ));
        } else {
            for (var file : request.getFiles()) {
                var imagePath = imageWriter.WriteImage(file);
                var image = new Image(imagePath);
                imageRepository.save(image);
                images.add(image);
            }
        }

        recipe.setImages(images);

        String username = auth.getName();
        recipe.setUser(userRepository.findByUsername(username).orElseThrow(
                ()-> new UserNotFoundException(String.format("user with username: %s, can't be found", username))
        ));

        recipe.getUser().getRoles().forEach(role ->{
            if (role.getName() == ERole.ROLE_ADMIN) recipe.setApproved();
        });

        return ResponseEntity.ok(new SimpleRecipeResponse(recipeRepository.save(recipe)));
    }

    @PutMapping(value = "/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveRecipe(@PathVariable Long id) {
        var recipe = recipeRepository.findById(id).orElseThrow(
                () -> new RecipeNotFoundException(String.format("recipe with id: %d, can't be found", id))
        );
        recipe.setApproved();
        recipeRepository.save(recipe);
        return ResponseEntity.ok(new SimpleRecipeResponse(recipe));
    }

    @DeleteMapping( value = "/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id, Authentication auth) {
        var recipe = recipeRepository.findById(id).orElseThrow(
                () -> new RecipeNotFoundException(String.format("recipe with id: %s, can't be found", id))
        );
        if(auth.getName().equals(recipe.getUser().getUsername()) ||
                auth.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
            for (var image : recipe.getImages()) {
                imageDeleter.DeleteImage(image.getLink());
            }
            recipeRepository.delete(recipe);
            return ResponseEntity.noContent().build();
        }
        throw new NoPermissionsException("you can't delete recipe that doesn't belong to you unless you are an administrator");
    }

}