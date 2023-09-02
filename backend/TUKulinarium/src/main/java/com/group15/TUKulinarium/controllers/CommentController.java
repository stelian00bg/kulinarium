package com.group15.TUKulinarium.controllers;

import com.group15.TUKulinarium.exception.CommentNotFoundException;
import com.group15.TUKulinarium.exception.NoPermissionsException;
import com.group15.TUKulinarium.exception.RecipeNotFoundException;
import com.group15.TUKulinarium.exception.UserNotFoundException;
import com.group15.TUKulinarium.models.Comment;
import com.group15.TUKulinarium.payload.request.AddCommentRequest;
import com.group15.TUKulinarium.repository.CommentRepository;
import com.group15.TUKulinarium.repository.RecipeRepository;
import com.group15.TUKulinarium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addCommentToRecipe(@Valid @RequestBody AddCommentRequest comment, Authentication auth)
        throws RecipeNotFoundException{
        var recipe = recipeRepository.findById(comment.getRecipeId()).orElseThrow(
                ()-> new RecipeNotFoundException(String.format("recipe with id: %d, can't be found", comment.getRecipeId()))
        );
        var commentRecord = new Comment(comment.getContent());
        commentRecord.setUser(userRepository.findByUsername(auth.getName()).orElseThrow(
                        () -> new UserNotFoundException(String.format("user with username: %s, can't be found", auth.getName()))
        ));
        recipe.getComments().add(commentRecord);
        commentRepository.save(commentRecord);
        recipeRepository.save(recipe);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, Authentication auth){
        var comment = commentRepository.findById(id).orElseThrow(
                () -> new CommentNotFoundException(String.format("comment with id: %d, can't be found", id))
        );
        if(auth.getName().equals(comment.getUser().getUsername()) ||
                auth.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
            commentRepository.delete(comment);
            return ResponseEntity.noContent().build();
        }
        throw new NoPermissionsException("you can't delete comment that doesn't belong to you unless you are an administrator");
    }

}
