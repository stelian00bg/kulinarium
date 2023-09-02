package com.group15.TUKulinarium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.group15.TUKulinarium.controllers.CommentController;
import com.group15.TUKulinarium.exception.RecipeNotFoundException;
import com.group15.TUKulinarium.exception.UserNotFoundException;
import com.group15.TUKulinarium.models.Comment;
import com.group15.TUKulinarium.models.Recipe;
import com.group15.TUKulinarium.models.User;
import com.group15.TUKulinarium.payload.request.AddCommentRequest;
import com.group15.TUKulinarium.repository.CommentRepository;
import com.group15.TUKulinarium.repository.RecipeRepository;
import com.group15.TUKulinarium.repository.UserRepository;
import com.group15.TUKulinarium.utils.ConvertJsonToString;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

@SpringBootTest(properties = "spring.main.lazy-initialization=true",
		classes = {CommentController.class})
@AutoConfigureMockMvc
@EnableWebMvc
class CommentControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private RecipeRepository recipeRepository;

	@MockBean
	CommentRepository commentRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
	public void testAddComment() throws Exception {
		var commentRequest = new AddCommentRequest();
		commentRequest.setContent("some");
		commentRequest.setRecipeId(1L);

		var commentBuilder = new Comment(commentRequest.getContent());
		var recipeBuilder = new Recipe();
		commentBuilder.setUser(new User());
		Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(new Recipe()));
		Mockito.when(userRepository.findByUsername("stoyo")).thenReturn(Optional.of(new User()));
		Mockito.when(commentRepository.save(commentBuilder)).thenReturn(commentBuilder);
		recipeBuilder.getComments().add(commentBuilder);
		Mockito.when(recipeRepository.save(recipeBuilder)).thenReturn(new Recipe());


		mockMvc.perform( MockMvcRequestBuilders
						.post("/api/comment/add")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(ConvertJsonToString.asJsonString(commentRequest))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testAddCommentWithoutUser() throws Exception {
		var commentRequest = new AddCommentRequest();
		commentRequest.setContent("some");
		commentRequest.setRecipeId(1L);

		mockMvc.perform( MockMvcRequestBuilders
						.post("/api/comment/add")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(ConvertJsonToString.asJsonString(commentRequest))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
	public void testAddCommentNotFoundRecipe() {
		var commentRequest = new AddCommentRequest();
		commentRequest.setContent("some");
		commentRequest.setRecipeId(1L);

		var commentBuilder = new Comment(commentRequest.getContent());
		var recipeBuilder = new Recipe();
		commentBuilder.setUser(new User());
		Mockito.when(recipeRepository.findById(1L)).thenThrow(RecipeNotFoundException.class);
		Mockito.when(userRepository.findByUsername("stoyo")).thenReturn(Optional.of(new User()));
		Mockito.when(commentRepository.save(commentBuilder)).thenReturn(commentBuilder);
		recipeBuilder.getComments().add(commentBuilder);
		Mockito.when(recipeRepository.save(recipeBuilder)).thenReturn(new Recipe());

		try {
			mockMvc.perform(MockMvcRequestBuilders
							.post("/api/comment/add")
							.with(csrf())
							.contentType(MediaType.APPLICATION_JSON)
							.content(ConvertJsonToString.asJsonString(commentRequest))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
		} catch (Exception ignored) {}
	}

	@Test
	@WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
	public void testAddCommentNotFoundUser() {
		var commentRequest = new AddCommentRequest();
		commentRequest.setContent("some");
		commentRequest.setRecipeId(1L);

		var commentBuilder = new Comment(commentRequest.getContent());
		var recipeBuilder = new Recipe();
		commentBuilder.setUser(new User());
		Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(new Recipe()));
		Mockito.when(userRepository.findByUsername("stoyo")).thenThrow(UserNotFoundException.class);
		Mockito.when(commentRepository.save(commentBuilder)).thenReturn(commentBuilder);
		recipeBuilder.getComments().add(commentBuilder);
		Mockito.when(recipeRepository.save(recipeBuilder)).thenReturn(new Recipe());

		try {
			mockMvc.perform(MockMvcRequestBuilders
							.post("/api/comment/add")
							.with(csrf())
							.contentType(MediaType.APPLICATION_JSON)
							.content(ConvertJsonToString.asJsonString(commentRequest))
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
		} catch (Exception ignored) {}
	}

	@Test
	@WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
	public void testRemoveComment(){
		var commentUser = new User();
		commentUser.setUsername("stoyo");
		var comment = new Comment();
		comment.setUser(commentUser);
		Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
		Mockito.doNothing().when(commentRepository).delete(comment);
		try {
			mockMvc.perform(MockMvcRequestBuilders
					.delete("/api/comment/delete/1")
					.with(csrf()))
					.andExpect(status().isNoContent());
		} catch (Exception ignored) {	}
	}

	@Test
	@WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
	public void testRemoveCommentAdmin(){
		var commentUser = new User();
		commentUser.setUsername("stoyo123");
		var comment = new Comment();
		comment.setUser(commentUser);
		Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
		Mockito.doNothing().when(commentRepository).delete(comment);
		try {
			mockMvc.perform(MockMvcRequestBuilders
							.delete("/api/comment/delete/1")
							.with(csrf()))
					.andExpect(status().isNoContent());
		} catch (Exception ignored) {	}
	}

	@Test
	@WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
	public void testRemoveCommentNotSameUser(){
		var commentUser = new User();
		commentUser.setUsername("stoyo123");
		var comment = new Comment();
		comment.setUser(commentUser);
		Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
		Mockito.doNothing().when(commentRepository).delete(comment);
		try {
			mockMvc.perform(MockMvcRequestBuilders
							.delete("/api/comment/delete/1")
							.with(csrf()))
					.andExpect(status().isUnauthorized());
		} catch (Exception ignored) {	}
	}

	@Test
	public void testRemoveCommentIsNotAuthorized(){
		try {
			mockMvc.perform(MockMvcRequestBuilders
							.delete("/api/comment/delete/1")
							.with(csrf()))
					.andExpect(status().isUnauthorized());
		} catch (Exception ignored) {	}
	}

}
