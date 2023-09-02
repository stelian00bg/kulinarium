package com.group15.TUKulinarium;

import com.group15.TUKulinarium.controllers.RecipeController;
import com.group15.TUKulinarium.exception.*;
import com.group15.TUKulinarium.models.Category;
import com.group15.TUKulinarium.models.Image;
import com.group15.TUKulinarium.models.Recipe;
import com.group15.TUKulinarium.models.User;
import com.group15.TUKulinarium.payload.request.CreateRecipeRequest;
import com.group15.TUKulinarium.repository.CategoryRepository;
import com.group15.TUKulinarium.repository.ImageRepository;
import com.group15.TUKulinarium.repository.RecipeRepository;
import com.group15.TUKulinarium.repository.UserRepository;
import com.group15.TUKulinarium.service.ImageDeleter;
import com.group15.TUKulinarium.service.ImageWriter;
import com.group15.TUKulinarium.utils.ConvertJsonToString;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.lazy-initialization=true",
        classes = {RecipeController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    ImageWriter imageWriter;

    @MockBean
    ImageDeleter imageDeleter;

    @MockBean
    RecipeRepository recipeRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    UserRepository userRepository;

    @Test
    void contextLoads(){
    }

    @Test
    public void testGetRecipeById(){
        Mockito.when(recipeRepository.findById(1L))
                .thenReturn(Optional.of(new Recipe()));
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("api/recipe/get/1")
                    .with(csrf()))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    public void testGetRecipeByIdRecipeNotFound(){
        Mockito.when(recipeRepository.findById(1L))
                .thenThrow(new RecipeNotFoundException(""));
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/recipe/get/1")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    public void testSearchRecipe(){
        Pageable pageable = PageRequest.of(0, 5);
        List<Recipe> recipeResults = new LinkedList<>();
        recipeResults.add(new Recipe());
        Mockito.when(recipeRepository.findPagedRecipes(pageable, "somename",
                        10000000, "", ""))
                .thenReturn(new PageImpl<Recipe>(recipeResults));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .get(
                            "api/recipe/search?name=somename&cookingTime=&username=&categoryName="
                    )
                    .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored) {}
    }

    @Test
    public void testSearchRecipeDefaultValues(){
        Pageable pageable = PageRequest.of(0, 5);
        List<Recipe> recipeResults = new LinkedList<>();
        recipeResults.add(new Recipe());
        Mockito.when(recipeRepository.findPagedRecipes(pageable, "",
                        10000000, "", ""))
                .thenReturn(new PageImpl<Recipe>(recipeResults));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get(
                                    "api/recipe/search?name=&cookingTime=&username=&categoryName="
                            )
                            .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored) {}
    }

    @Test
    public void testSearchRecipeParametersIgnoredCase(){
        Pageable pageable = PageRequest.of(0, 5);
        List<Recipe> recipeResults = new LinkedList<>();
        recipeResults.add(new Recipe());
        Mockito.when(recipeRepository.findPagedRecipes(pageable, "somename",
                        10000000, "someusername", "somecategory"))
                .thenReturn(new PageImpl<Recipe>(recipeResults));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get(
                                    "api/recipe/search?name=SOMENAME&cookingTime=&username=SOMEUSERNAME&categoryName=SOMECATEGORY"
                            )
                            .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored) {}
    }

    @Test
    public void testSearchRecipeCookingTimeParameter(){
        Pageable pageable = PageRequest.of(0, 5);
        List<Recipe> recipeResults = new LinkedList<>();
        recipeResults.add(new Recipe());
        Mockito.when(recipeRepository.findPagedRecipes(pageable, "",
                        1, "", ""))
                .thenReturn(new PageImpl<Recipe>(recipeResults));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get(
                                    "api/recipe/search?name=&cookingTime=1&username=&categoryName="
                            )
                            .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored) {}
    }

    @Test
    public void testSearchRecipeNotFoundAny(){
        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(recipeRepository.findPagedRecipes(pageable, "",
                        10000000, "", ""))
                .thenReturn(Page.empty());
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get(
                                    "api/recipe/search?name=&cookingTime=&username=&categoryName="
                            )
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testRecipesPendingApproval(){
        Pageable pageable = PageRequest.of(0, 5);
        List<Recipe> recipeResults = new LinkedList<>();
        recipeResults.add(new Recipe());
        Mockito.when(recipeRepository.findPagedPendingApproval(pageable))
                .thenReturn(new PageImpl<>(recipeResults));

        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .get("api/recipe/pending?currentPage=&perPage")
                    .with(csrf())
            )
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testRecipesPendingApprovalNotFoundAny(){
        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(recipeRepository.findPagedPendingApproval(pageable))
                .thenReturn(Page.empty());

        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/recipe/pending?currentPage=&perPage")
                            .with(csrf())
                    )
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testRecipesPendingApprovalUserShouldNotAccess(){
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/recipe/pending?currentPage=&perPage")
                            .with(csrf())
                    )
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) {}
    }


    @Test
    public void testRecipesGetComments(){
        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(recipeRepository.findById(1L))
                .thenReturn(Optional.of(new Recipe()));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/recipe/comments?recipeId=1")
                            .with(csrf())
                    )
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    public void testRecipesGetCommentsRecipeNotFound(){
        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(recipeRepository.findById(1L))
                .thenThrow(new RecipeNotFoundException(""));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/recipe/comments?recipeId=1")
                            .with(csrf())
                    )
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testAddRecipe(){
        CreateRecipeRequest crr = new CreateRecipeRequest();
        crr.setInstructions("someinst");
        crr.setIngredients("someingr");
        crr.setCookingTime(123);
        crr.setCategory("somecat");
        crr.setName("somename");
        crr.setFiles(new MultipartFile[]{ new MockMultipartFile("somename", new byte[]{})});
        Category cat = new Category("somecat");
        Image img = new Image("somelink");
        User user = new User();
        user.setUsername("stoyo");
        Recipe recipe = new Recipe();
        recipe.setUser(user);
        recipe.setInstructions(crr.getInstructions());
        recipe.setNotApproved();
        recipe.setIngredients(crr.getIngredients());
        recipe.setImages(List.of(new Image("someimage")));
        recipe.setName("somename");

        Mockito.when(categoryRepository.findByName("somecat"))
                .thenReturn(Optional.of(cat));
        Mockito.when(imageRepository.findById(1L))
                .thenReturn(Optional.of(new Image()));
        try {
            Mockito.when(imageWriter.WriteImage(crr.getFiles()[0]))
                    .thenReturn("image");
        } catch (ImageWriterException ignored) { }
        Mockito.when(imageRepository.save(new Image("image")))
                .thenReturn(new Image("image"));
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(user));
        Mockito.when(recipeRepository.save(recipe))
                .thenReturn(recipe);
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .post("api/recipe/add")
                    .with(csrf())
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .content(ConvertJsonToString.asJsonString(crr)))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testAddRecipeNotValidCookingTime(){
        CreateRecipeRequest crr = new CreateRecipeRequest();
        crr.setInstructions("someinst");
        crr.setIngredients("someingr");
        crr.setCookingTime(44232323);
        crr.setCategory("somecat");
        crr.setName("somename");
        crr.setFiles(new MultipartFile[]{ new MockMultipartFile("somename", new byte[]{})});
        Category cat = new Category("somecat");
        Image img = new Image("somelink");
        User user = new User();
        user.setUsername("stoyo");
        Recipe recipe = new Recipe();
        recipe.setUser(user);
        recipe.setInstructions(crr.getInstructions());
        recipe.setNotApproved();
        recipe.setIngredients(crr.getIngredients());
        recipe.setImages(List.of(new Image("someimage")));
        recipe.setName("somename");

        Mockito.when(categoryRepository.findByName("somecat"))
                .thenReturn(Optional.of(cat));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/recipe/add")
                            .with(csrf())
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .content(ConvertJsonToString.asJsonString(crr)))
                    .andExpect(status().isBadRequest());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testAddRecipeCategoryNotFound(){
        CreateRecipeRequest crr = new CreateRecipeRequest();
        crr.setInstructions("someinst");
        crr.setIngredients("someingr");
        crr.setCookingTime(123);
        crr.setCategory("somecat");
        crr.setName("somename");
        crr.setFiles(new MultipartFile[]{ new MockMultipartFile("somename", new byte[]{})});

        Mockito.when(categoryRepository.findByName("somecat"))
                .thenThrow(new CategoryNotFoundException(""));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/recipe/add")
                            .with(csrf())
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .content(ConvertJsonToString.asJsonString(crr)))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testAddRecipeDefaultImageNotFound(){
        CreateRecipeRequest crr = new CreateRecipeRequest();
        crr.setInstructions("someinst");
        crr.setIngredients("someingr");
        crr.setCookingTime(123);
        crr.setCategory("somecat");
        crr.setName("somename");
        crr.setFiles(null);
        Category cat = new Category("somecat");

        Mockito.when(categoryRepository.findByName("somecat"))
                .thenReturn(Optional.of(cat));
        Mockito.when(imageRepository.findById(1L))
                .thenThrow(new ImageNotFoundException(""));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/recipe/add")
                            .with(csrf())
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .content(ConvertJsonToString.asJsonString(crr)))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testAddRecipeImageWriterException(){
        CreateRecipeRequest crr = new CreateRecipeRequest();
        crr.setInstructions("someinst");
        crr.setIngredients("someingr");
        crr.setCookingTime(123);
        crr.setCategory("somecat");
        crr.setName("somename");
        crr.setFiles(new MultipartFile[]{ new MockMultipartFile("somename", new byte[]{})});
        Category cat = new Category("somecat");

        Mockito.when(categoryRepository.findByName("somecat"))
                .thenReturn(Optional.of(cat));
        try {
            Mockito.when(imageWriter.WriteImage(crr.getFiles()[0]))
                    .thenThrow(new ImageWriterException("ss"));
        } catch (ImageWriterException ignored) { }

        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/recipe/add")
                            .with(csrf())
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .content(ConvertJsonToString.asJsonString(crr)))
                    .andExpect(status().isInternalServerError());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testAddRecipeUserNotFound(){
        CreateRecipeRequest crr = new CreateRecipeRequest();
        crr.setInstructions("someinst");
        crr.setIngredients("someingr");
        crr.setCookingTime(123);
        crr.setCategory("somecat");
        crr.setName("somename");
        crr.setFiles(new MultipartFile[]{ new MockMultipartFile("somename", new byte[]{})});
        Category cat = new Category("somecat");
        Image img = new Image("somelink");
        User user = new User();
        user.setUsername("stoyo");
        Recipe recipe = new Recipe();
        recipe.setUser(user);
        recipe.setInstructions(crr.getInstructions());
        recipe.setNotApproved();
        recipe.setIngredients(crr.getIngredients());
        recipe.setImages(List.of(new Image("someimage")));
        recipe.setName("somename");

        Mockito.when(categoryRepository.findByName("somecat"))
                .thenReturn(Optional.of(cat));
        Mockito.when(imageRepository.findById(1L))
                .thenReturn(Optional.of(new Image()));
        try {
            Mockito.when(imageWriter.WriteImage(crr.getFiles()[0]))
                    .thenReturn("image");
        } catch (ImageWriterException ignored) { }
        Mockito.when(imageRepository.save(new Image("image")))
                .thenReturn(new Image("image"));
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenThrow(new UserNotFoundException(""));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/recipe/add")
                            .with(csrf())
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .content(ConvertJsonToString.asJsonString(crr)))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testApproveRecipeAdmin(){
        Recipe recipe = new Recipe();
        Mockito.when(recipeRepository.findById(1L))
                .thenReturn(Optional.of(recipe));
        Mockito.when(recipeRepository.save(recipe))
                .thenReturn(recipe);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/recipe/approve/1")
                            .with(csrf()))
                    .andExpect(status().isOk());
        } catch (Exception ignored) { }
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testApproveRecipeUserShouldNotHavePermissions(){
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .put("api/recipe/approve/1")
                    .with(csrf()))
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) { }
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testApproveRecipeNotFound(){
        Recipe recipe = new Recipe();
        Mockito.when(recipeRepository.findById(1L))
                .thenThrow(new RecipeNotFoundException(""));

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/recipe/approve/1")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) { }
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testDeleteRecipe(){
        Recipe recipe = new Recipe();
        Image image = new Image();
        image.setLink("somelink");
        List<Image> images = new LinkedList<>();
        images.add(image);
        recipe.setImages(images);

        Mockito.when(recipeRepository.findById(1L))
                .thenReturn(Optional.of(recipe));
        Mockito.doNothing().when(imageDeleter).DeleteImage("somelink");
        Mockito.doNothing().when(recipeRepository).delete(recipe);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/recipe/delete/1")
                            .with(csrf()))
                    .andExpect(status().isNoContent());
        } catch (Exception ignored) { }
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testDeleteRecipeNotSameUser(){
        Recipe recipe = new Recipe();
        Image image = new Image();
        image.setLink("somelink");
        List<Image> images = new LinkedList<>();
        images.add(image);
        recipe.setImages(images);
        User user = new User();
        user.setName("somename");
        recipe.setUser(user);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/recipe/delete/1")
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) { }
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testDeleteRecipeSameUser(){
        Recipe recipe = new Recipe();
        Image image = new Image();
        image.setLink("somelink");
        List<Image> images = new LinkedList<>();
        images.add(image);
        recipe.setImages(images);
        User user = new User();
        user.setName("stoyo");
        recipe.setUser(user);

        Mockito.when(recipeRepository.findById(1L))
            .thenReturn(Optional.of(recipe));
        Mockito.doNothing().when(imageDeleter).DeleteImage("somelink");
        Mockito.doNothing().when(recipeRepository).delete(recipe);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/recipe/delete/1")
                            .with(csrf()))
                    .andExpect(status().isNoContent());
        } catch (Exception ignored) { }
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testDeleteRecipeNotFoundRecipe(){
        Recipe recipe = new Recipe();
        Image image = new Image();
        image.setLink("somelink");
        List<Image> images = new LinkedList<>();
        images.add(image);
        recipe.setImages(images);
        User user = new User();
        user.setName("stoyo");
        recipe.setUser(user);

        Mockito.when(recipeRepository.findById(1L))
                .thenThrow(new RecipeNotFoundException(""));

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/recipe/delete/1")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) { }
    }
}
