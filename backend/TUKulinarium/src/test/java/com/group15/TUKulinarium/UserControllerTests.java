package com.group15.TUKulinarium;

import com.group15.TUKulinarium.controllers.UserController;
import com.group15.TUKulinarium.exception.ImageWriterException;
import com.group15.TUKulinarium.exception.RoleNotFoundException;
import com.group15.TUKulinarium.exception.UserNotFoundException;
import com.group15.TUKulinarium.models.*;
import com.group15.TUKulinarium.payload.request.ChangeProfilePictureRequest;
import com.group15.TUKulinarium.payload.request.CreateCategoryRequest;
import com.group15.TUKulinarium.payload.request.UpdateEmailRequest;
import com.group15.TUKulinarium.payload.request.UpdatePasswordRequest;
import com.group15.TUKulinarium.repository.*;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.lazy-initialization=true",
        classes = {UserController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    ImageWriter imageWriter;

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetUserById() {
        Mockito.when(userRepository.getById(1L)).thenReturn(new User());
        try {
            mockMvc.perform(get("api/user/get/1"))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    public void testGetUserByIdNotFound() {
        Mockito.when(userRepository.getById(1L)).thenThrow(new UserNotFoundException("sss"));
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/user/get/1")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored) {}
    }

    @Test
    public void testSearchUser(){
        Pageable pageable = PageRequest.of(0, 5);
        List<User> userResults = new LinkedList<>();
        userResults.add(new User());
        Mockito.when(userRepository.findPagedUsers(pageable, "ss", "s", "s@email.com"))
                .thenReturn(new PageImpl<User>(userResults));
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/user/search?username=ss&name=s&email=s@email.com")
                            .with(csrf()))
                    .andExpect(status().isOk());
        } catch (Exception ignored){}
    }

    @Test
    public void testSearchUserNotFoundAny(){
        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(userRepository.findPagedUsers(pageable, "ss", "s", "s@email.com"))
                .thenReturn(Page.empty());
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("api/user/search?username=ss&name=s&email=s@email.com")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testMakeAdmin(){
        User adminUser = new User();
        User targetUser = new User();
        Role adminRole = new Role(ERole.ROLE_ADMIN);
        adminUser.getRoles().add(adminRole);
        adminUser.setRoles(adminUser.getRoles());
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(adminUser));
        Mockito.when(userRepository.findByUsername("stoyo123"))
                .thenReturn(Optional.of(targetUser));
        Mockito.when(roleRepository.findByName(ERole.ROLE_ADMIN))
                .thenReturn(Optional.of(adminRole));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .put("/api/user/makeAdmin?username=stoyo123")
                    .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testMakeAdminCurrUserNotFound(){
        User adminUser = new User();
        User targetUser = new User();
        Role adminRole = new Role(ERole.ROLE_ADMIN);
        adminUser.getRoles().add(adminRole);
        adminUser.setRoles(adminUser.getRoles());
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenThrow(new UserNotFoundException("stoyo"));
        Mockito.when(userRepository.findByUsername("stoyo123"))
                .thenReturn(Optional.of(targetUser));
        Mockito.when(roleRepository.findByName(ERole.ROLE_ADMIN))
                .thenReturn(Optional.of(adminRole));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/user/makeAdmin?username=stoyo123")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testMakeAdminTargetUserNotFound(){
        User adminUser = new User();
        User targetUser = new User();
        Role adminRole = new Role(ERole.ROLE_ADMIN);
        adminUser.getRoles().add(adminRole);
        adminUser.setRoles(adminUser.getRoles());
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(adminUser));
        Mockito.when(userRepository.findByUsername("stoyo123"))
                .thenThrow(new UserNotFoundException("stoyo123"));
        Mockito.when(roleRepository.findByName(ERole.ROLE_ADMIN))
                .thenReturn(Optional.of(adminRole));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/user/makeAdmin?username=stoyo123")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testMakeAdminAdminRoleNotFound(){
        User adminUser = new User();
        User targetUser = new User();
        Role adminRole = new Role(ERole.ROLE_ADMIN);
        adminUser.getRoles().add(adminRole);
        adminUser.setRoles(adminUser.getRoles());
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(adminUser));
        Mockito.when(userRepository.findByUsername("stoyo123"))
                .thenReturn(Optional.of(targetUser));
        Mockito.when(roleRepository.findByName(ERole.ROLE_ADMIN))
                .thenThrow(new RoleNotFoundException(ERole.ROLE_ADMIN));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/user/makeAdmin?username=stoyo123")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testMakeAdminUnauthorizedUser(){
        User adminUser = new User();
        User targetUser = new User();
        Role userRole = new Role(ERole.ROLE_USER);
        adminUser.getRoles().add(userRole);
        adminUser.setRoles(adminUser.getRoles());
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(adminUser));
        Mockito.when(userRepository.findByUsername("stoyo123"))
                .thenReturn(Optional.of(targetUser));
        Mockito.when(roleRepository.findByName(ERole.ROLE_USER))
                .thenReturn(Optional.of(userRole));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/user/makeAdmin?username=stoyo123")
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }catch (Exception ignored){}
    }

    @Test
    public void testMakeAdminWithoutUser(){
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/user/makeAdmin?username=stoyo123")
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void updateProfilePictureOfAUser() {
        ChangeProfilePictureRequest cprq = new ChangeProfilePictureRequest();
        cprq.setPicture(new MockMultipartFile("some name", new byte[]{}));

        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(new User()));

        try {
            Mockito.when(imageWriter.WriteImage(cprq.getPicture()))
                    .thenReturn("somelink");
        } catch (ImageWriterException e) {
            e.printStackTrace();
        }
        Mockito.when(imageRepository.save(new Image("somelink")))
                .thenReturn(new Image("somelink"));
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("api/user/updateProfilePicture")
                            .file("picture", cprq.getPicture().getBytes()))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void updateProfilePictureOfAUserNotFoundUser() {
        ChangeProfilePictureRequest cprq = new ChangeProfilePictureRequest();
        cprq.setPicture(new MockMultipartFile("some name", new byte[]{}));

        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenThrow(new UserNotFoundException("stoyo"));

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("api/user/updateProfilePicture")
                            .file("picture", cprq.getPicture().getBytes()))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void updateProfilePictureOfAUserImageWriterError() {
        ChangeProfilePictureRequest cprq = new ChangeProfilePictureRequest();
        cprq.setPicture(new MockMultipartFile("some name", new byte[]{}));

        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(new User()));

        try {
            Mockito.when(imageWriter.WriteImage(cprq.getPicture()))
                    .thenThrow(new ImageWriterException(""));
        } catch (ImageWriterException e) {
            e.printStackTrace();
        }
        Mockito.when(imageRepository.save(new Image("somelink")))
                .thenReturn(new Image("somelink"));
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("api/user/updateProfilePicture")
                            .file("picture", cprq.getPicture().getBytes()))
                    .andExpect(status().isInternalServerError());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void updateName(){
        User user = new User();
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .put("api/user/updateName?newName=asd")
                    .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void updateNameAdmin(){
        User user = new User();
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updateName?newName=asd")
                            .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void updateNameUserNotFound(){
        User user = new User();
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenThrow(new UserNotFoundException("stoyo"));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updateName?newName=asd")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored) {}
    }

    @Test
    public void updateNameWithoutAuthorization(){
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updateName?newName=asd")
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void updateUsername(){
        User user = new User();
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(new User()));
        Mockito.when(userRepository.existsByUsername("stoyo123"))
                .thenReturn(false);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .put("api/user/updateUsername?newUsername=stoyo123")
                    .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void updateUsernameUserNotFound(){
        User user = new User();
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenThrow(new UserNotFoundException("stoyo"));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updateUsername?newUsername=stoyo123")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void updateUsernameNewUsernameExists(){
        User user = new User();
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(new User()));
        Mockito.when(userRepository.existsByUsername("stoyo123"))
                .thenReturn(true);
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updateUsername?newUsername=stoyo123")
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }catch (Exception ignored) {}
    }

    @Test
    public void updateUsernameUnauthorized(){
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updateUsername?newUsername=stoyo123")
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testUpdateEmail(){
        User user = new User();
        UpdateEmailRequest req = new UpdateEmailRequest();
        req.setNewEmail("e@email.com");
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByEmail(req.getNewEmail()))
                .thenReturn(false);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .put("api/user/updateEmail")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ConvertJsonToString.asJsonString(req))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testUpdateEmailUserNotFound(){
        User user = new User();
        UpdateEmailRequest req = new UpdateEmailRequest();
        req.setNewEmail("e@email.com");
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenThrow(new UserNotFoundException("stoyo"));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updateEmail")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(req))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testUpdateEmailWithEmailThatExists(){
        User user = new User();
        UpdateEmailRequest req = new UpdateEmailRequest();
        req.setNewEmail("e@email.com");
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByEmail(req.getNewEmail()))
                .thenReturn(true);
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updateEmail")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(req))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }catch (Exception ignored){}
    }

    @Test
    public void testUpdateEmailUnauthorized(){
        User user = new User();
        UpdateEmailRequest req = new UpdateEmailRequest();
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updateEmail")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(req))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }catch (Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testUpdatePassword(){
        UpdatePasswordRequest upr = new UpdatePasswordRequest();
        upr.setCurrentPassword("default");
        upr.setNewPassword("asd1234");
        upr.setConfirmNewPassword("asd1234");

        User user = new User();

        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .put("api/user/updatePassword")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ConvertJsonToString.asJsonString(upr))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch(Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testUpdatePasswordUserNotFound(){
        UpdatePasswordRequest upr = new UpdatePasswordRequest();
        upr.setCurrentPassword("default");
        upr.setNewPassword("asd1234");
        upr.setConfirmNewPassword("asd1234");

        User user = new User();

        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenThrow(new UserNotFoundException("stoyo"));
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updatePassword")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(upr))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch(Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testUpdatePasswordWrongPassword(){
        UpdatePasswordRequest upr = new UpdatePasswordRequest();
        upr.setCurrentPassword("default321312");
        upr.setNewPassword("asd1234");
        upr.setConfirmNewPassword("asd1234");

        User user = new User();

        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updatePassword")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(upr))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch(Exception ignored){}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_USER")
    public void testUpdatePasswordNewPasswordsMismatchs(){
        UpdatePasswordRequest upr = new UpdatePasswordRequest();
        upr.setCurrentPassword("default");
        upr.setNewPassword("asd12345");
        upr.setConfirmNewPassword("asd1234");

        User user = new User();

        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("api/user/updatePassword")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(upr))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch(Exception ignored){}
    }
}
