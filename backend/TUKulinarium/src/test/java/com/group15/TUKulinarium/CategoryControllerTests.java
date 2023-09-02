package com.group15.TUKulinarium;

import com.group15.TUKulinarium.controllers.CategoryController;
import com.group15.TUKulinarium.exception.CategoryNotFoundException;
import com.group15.TUKulinarium.exception.ImageNotFoundException;
import com.group15.TUKulinarium.exception.ImageWriterException;
import com.group15.TUKulinarium.models.Category;
import com.group15.TUKulinarium.models.Image;
import com.group15.TUKulinarium.models.User;
import com.group15.TUKulinarium.payload.request.CreateCategoryRequest;
import com.group15.TUKulinarium.repository.CategoryRepository;
import com.group15.TUKulinarium.repository.ImageRepository;
import com.group15.TUKulinarium.service.ImageWriter;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.lazy-initialization=true",
        classes = {CategoryController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    ImageWriter imageWriter;

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetCategoryById(){
        Mockito.when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(new Category()));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .get("api/category/get/1")
                    .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored) {}
    }

    @Test
    public void testGetCategoryByIdNotFound(){
        Mockito.when(categoryRepository.findById(1L))
                .thenThrow(new CategoryNotFoundException("asd"));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/category/get/1")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored) {}
    }

    @Test
    public void testSearchCategory(){
        Pageable pageable = PageRequest.of(0, 5);
        List<Category> categoriesResults = new LinkedList<>();
        categoriesResults.add(new Category());
        Mockito.when(categoryRepository.findPagedCategories(pageable, "somename"))
                .thenReturn(new PageImpl<Category>(categoriesResults));
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/category/search?name=somename")
                            .with(csrf()))
                    .andExpect(status().isOk());
        } catch (Exception ignored){}
    }

    @Test
    public void testSearchCategoryNotFoundAny(){
        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(categoryRepository.findPagedCategories(pageable, "somename"))
                .thenThrow(new CategoryNotFoundException(""));
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/category/search?name=somename")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void createCategory() {
        CreateCategoryRequest ccrq = new CreateCategoryRequest();
        ccrq.setImage(new MockMultipartFile("some name", new byte[]{}));
        ccrq.setName("somecategory");
        Category category = new Category();
        category.setName("some name");
        category.setImage(new Image("somelink"));

        Mockito.when(imageRepository.findById(1L))
                .thenReturn(Optional.of(new Image()));
        try {
            Mockito.when(imageWriter.WriteImage(ccrq.getImage()))
                    .thenReturn("somelink");
        } catch (ImageWriterException e) {
            e.printStackTrace();
        }
        Mockito.when(imageRepository.save(new Image("somelink")))
                .thenReturn(new Image("somelink"));
        Mockito.when(categoryRepository.save(category))
                .thenReturn(category);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .multipart("api/category/create")
                    .file("files", ccrq.getImage().getBytes())
                    .param("name", "somename"))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    public void createCategoryUnauthorized() {
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("api/category/create")
                            .file("files", new byte[]{})
                            .param("name", "somename"))
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void createCategoryNullImage() {
        CreateCategoryRequest ccrq = new CreateCategoryRequest();
        ccrq.setImage(new MockMultipartFile("some name", new byte[]{}));
        ccrq.setName("somecategory");
        Category category = new Category();
        category.setName("some name");
        category.setImage(new Image("somelink"));

        Mockito.when(imageRepository.findById(1L))
                .thenReturn(Optional.of(new Image()));
        try {
            Mockito.when(imageWriter.WriteImage(ccrq.getImage()))
                    .thenReturn("somelink");
        } catch (ImageWriterException e) {
            e.printStackTrace();
        }
        Mockito.when(imageRepository.save(new Image("somelink")))
                .thenReturn(new Image("somelink"));
        Mockito.when(categoryRepository.save(category))
                .thenReturn(category);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("api/category/create")
                            .file("files", null)
                            .param("name", "somename"))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void createCategoryNullImageDefaultImageNotFound() {
        CreateCategoryRequest ccrq = new CreateCategoryRequest();
        ccrq.setImage(new MockMultipartFile("some name", new byte[]{}));
        ccrq.setName("somecategory");
        Category category = new Category();
        category.setName("some name");
        category.setImage(new Image("somelink"));

        Mockito.when(imageRepository.findById(1L))
                .thenThrow(new ImageNotFoundException("ss"));
        try {
            Mockito.when(imageWriter.WriteImage(ccrq.getImage()))
                    .thenReturn("somelink");
        } catch (ImageWriterException e) {
            e.printStackTrace();
        }
        Mockito.when(imageRepository.save(new Image("somelink")))
                .thenReturn(new Image("somelink"));
        Mockito.when(categoryRepository.save(category))
                .thenReturn(category);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("api/category/create")
                            .file("files", null)
                            .param("name", "somename"))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void createCategoryImageWriteServiceError() {
        CreateCategoryRequest ccrq = new CreateCategoryRequest();
        ccrq.setImage(new MockMultipartFile("some name", new byte[]{}));
        ccrq.setName("somecategory");
        Category category = new Category();
        category.setName("some name");
        category.setImage(new Image("somelink"));

        Mockito.when(imageRepository.findById(1L))
                .thenReturn(Optional.of(new Image()));
        try {
            Mockito.when(imageWriter.WriteImage(ccrq.getImage()))
                    .thenThrow(new ImageWriterException("sss"));
        } catch (ImageWriterException e) {
            e.printStackTrace();
        }
        Mockito.when(imageRepository.save(new Image("somelink")))
                .thenReturn(new Image("somelink"));
        Mockito.when(categoryRepository.save(category))
                .thenReturn(category);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("api/category/create")
                            .file("files", ccrq.getImage().getBytes())
                            .param("name", "somename"))
                    .andExpect(status().isInternalServerError());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testDeleteCategory(){
        Category categoryToBeDelete = new Category();
        Mockito.when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(categoryToBeDelete));
        Mockito.doNothing().when(categoryRepository).
                delete(categoryToBeDelete);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("api/category/delete/1")
                            .with(csrf()))
                    .andExpect(status().isNoContent());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testDeleteCategoryNotFound(){
        Category categoryToBeDelete = new Category();
        Mockito.when(categoryRepository.findById(1L))
                .thenThrow(new CategoryNotFoundException("something"));
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("api/category/delete/1")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testDeleteCategoryNotAdmin(){
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("api/category/delete/1")
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) {}
    }

    @Test
    public void testDeleteCategoryWithoutLogin(){
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("api/category/delete/1")
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) {}
    }
}
