package com.group15.TUKulinarium.controllers;

import com.group15.TUKulinarium.exception.CategoryNotFoundException;
import com.group15.TUKulinarium.exception.ImageNotFoundException;
import com.group15.TUKulinarium.exception.ImageWriterException;
import com.group15.TUKulinarium.models.Category;
import com.group15.TUKulinarium.models.Image;
import com.group15.TUKulinarium.payload.request.CreateCategoryRequest;
import com.group15.TUKulinarium.payload.response.SimpleCategoryResponse;
import com.group15.TUKulinarium.repository.CategoryRepository;
import com.group15.TUKulinarium.repository.ImageRepository;
import com.group15.TUKulinarium.service.ImageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageWriter imageWriter;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        var category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(String.format("category with id: %d, can't be found", id))
        );
        return ResponseEntity.ok(new SimpleCategoryResponse(category));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchCategoriesByName(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "perPage", defaultValue = "5") int perPage,
            @RequestParam(value = "name", required = false, defaultValue = "") String name
    ){
        Pageable pageable = PageRequest.of(currentPage-1, perPage);
        Page<Category> categories = categoryRepository.findPagedCategories(pageable, name.toLowerCase());
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException(String.format("there aren't any categories with name like: %s", name));
        }
        List<SimpleCategoryResponse> categoryResponseList = new LinkedList<>();
        categories.forEach(category -> {
            categoryResponseList.add(new SimpleCategoryResponse(category));
        });
        return ResponseEntity.ok(categoryResponseList);
    }

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(@Valid @ModelAttribute CreateCategoryRequest request) throws ImageWriterException {
        var categoryObj = new Category();
        categoryObj.setName(request.getName());
        if(request.getImage() == null) {
            categoryObj.setImage(imageRepository.findById(1L).orElseThrow(
                    () -> new ImageNotFoundException(String.format("image with id: %d, can't be found", 1))
            ));
        } else {
            String imageLink = imageWriter.WriteImage(request.getImage());
            categoryObj.setImage(imageRepository.save(new Image(imageLink)));
        }
        var categoryRecord = categoryRepository.save(categoryObj);
        return ResponseEntity.ok(categoryRecord);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryRepository.delete(categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(String.format("category with id: %d, can't be found", id))
        ));
        return ResponseEntity.noContent().build();
    }
}
