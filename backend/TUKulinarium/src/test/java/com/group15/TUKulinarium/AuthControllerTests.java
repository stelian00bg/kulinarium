package com.group15.TUKulinarium;

import com.group15.TUKulinarium.controllers.AuthController;
import com.group15.TUKulinarium.exception.ImageNotFoundException;
import com.group15.TUKulinarium.exception.RoleNotFoundException;
import com.group15.TUKulinarium.exception.TokenRefreshException;
import com.group15.TUKulinarium.exception.UserNotFoundException;
import com.group15.TUKulinarium.models.*;
import com.group15.TUKulinarium.payload.request.LoginRequest;
import com.group15.TUKulinarium.payload.request.SignupRequest;
import com.group15.TUKulinarium.repository.ImageRepository;
import com.group15.TUKulinarium.repository.RoleRepository;
import com.group15.TUKulinarium.repository.UserRepository;
import com.group15.TUKulinarium.security.services.RefreshTokenService;
import com.group15.TUKulinarium.utils.ConvertJsonToString;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.lazy-initialization=true",
        classes = {AuthController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    RefreshTokenService refreshTokenService;

    @MockBean
    PasswordEncoder encoder;

    @Test
    public void contextLoads(){
        Mockito.when(encoder.encode("somepass"))
                .thenReturn("test");
    }

    @Test
    public void testLogin(){
        Authentication auth = Mockito.mock(Authentication.class);
        LoginRequest lr = new LoginRequest();
        lr.setUsername("someusername");
        lr.setUsername("somepassword");
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(lr.getUsername(), lr.getPassword())))
                .thenReturn(auth);
        Mockito.when(refreshTokenService.createRefreshToken(1L))
                .thenReturn(new RefreshToken());
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(lr)))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    public void testLoginBadCredentials(){
        Authentication auth = Mockito.mock(Authentication.class);
        LoginRequest lr = new LoginRequest();
        lr.setUsername("someusername");
        lr.setUsername("somepassword");
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(lr.getUsername(), lr.getPassword())))
                .thenThrow(new BadCredentialsException(""));
        Mockito.when(refreshTokenService.createRefreshToken(1L))
                .thenReturn(new RefreshToken());
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(lr)))
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) {}
    }

    @Test
    public void testLoginDisabledException(){
        Authentication auth = Mockito.mock(Authentication.class);
        LoginRequest lr = new LoginRequest();
        lr.setUsername("someusername");
        lr.setUsername("somepassword");
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(lr.getUsername(), lr.getPassword())))
                .thenThrow(new DisabledException(""));
        Mockito.when(refreshTokenService.createRefreshToken(1L))
                .thenReturn(new RefreshToken());
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(lr)))
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) {}
    }

    @Test
    public void testLoginLockedException(){
        Authentication auth = Mockito.mock(Authentication.class);
        LoginRequest lr = new LoginRequest();
        lr.setUsername("someusername");
        lr.setUsername("somepassword");
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(lr.getUsername(), lr.getPassword())))
                .thenThrow(new LockedException(""));
        Mockito.when(refreshTokenService.createRefreshToken(1L))
                .thenReturn(new RefreshToken());
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(lr)))
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) {}
    }

    @Test
    public void testLoginRefreshTokenException(){
        Authentication auth = Mockito.mock(Authentication.class);
        LoginRequest lr = new LoginRequest();
        lr.setUsername("someusername");
        lr.setUsername("somepassword");
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(lr.getUsername(), lr.getPassword())))
                .thenReturn(auth);
        Mockito.when(refreshTokenService.createRefreshToken(1L))
                .thenThrow(new UserNotFoundException(""));
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(lr)))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    public void testSignUp(){
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = new User();

        SignupRequest sur = new SignupRequest();
        sur.setEmail("someemail@mail.com");
        sur.setUsername("someusername");
        sur.setName("somename");
        sur.setPassword("somepass");

        user.setName(sur.getEmail());
        user.setImage(new Image());
        user.setUsername(sur.getUsername());
        user.setEmail(sur.getEmail());
        user.setPassword("asd");
        user.setRoles(roles);

        Mockito.when(userRepository.existsByEmail(sur.getUsername()))
                .thenReturn(false);
        Mockito.when(userRepository.existsByEmail(sur.getEmail()))
                .thenReturn(false);
        Mockito.when(imageRepository.getById(1L))
                .thenReturn(new Image());
        Mockito.when(roleRepository.findByName(ERole.ROLE_ADMIN))
                .thenReturn(Optional.of(role));
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/sugnup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(sur)))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    public void testSignUpUsernameExists(){
        SignupRequest sur = new SignupRequest();
        sur.setUsername("someusername");

        Mockito.when(userRepository.existsByUsername(sur.getUsername()))
                .thenReturn(true);

        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/sugnup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(sur)))
                    .andExpect(status().isBadRequest());
        } catch (Exception ignored) {}
    }

    @Test
    public void testSignUpExistByEmail(){
        SignupRequest sur = new SignupRequest();
        sur.setEmail("someemail@mail.com");
        sur.setUsername("someusername");

        Mockito.when(userRepository.existsByEmail(sur.getUsername()))
                .thenReturn(false);
        Mockito.when(userRepository.existsByEmail(sur.getEmail()))
                .thenReturn(true);

        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/sugnup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(sur)))
                    .andExpect(status().isBadRequest());
        } catch (Exception ignored) {}
    }

    @Test
    public void testSignUpDefaultImageNotFound(){
        SignupRequest sur = new SignupRequest();
        sur.setEmail("someemail@mail.com");
        sur.setUsername("someusername");
        sur.setName("somename");
        sur.setPassword("somepass");

        Mockito.when(userRepository.existsByEmail(sur.getUsername()))
                .thenReturn(false);
        Mockito.when(userRepository.existsByEmail(sur.getEmail()))
                .thenReturn(false);
        Mockito.when(imageRepository.getById(1L))
                .thenThrow(new ImageNotFoundException(""));

        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/sugnup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(sur)))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    public void testSignUpRoleNotFound(){

        SignupRequest sur = new SignupRequest();
        sur.setEmail("someemail@mail.com");
        sur.setUsername("someusername");
        sur.setName("somename");
        sur.setPassword("somepass");

        Mockito.when(userRepository.existsByEmail(sur.getUsername()))
                .thenReturn(false);
        Mockito.when(userRepository.existsByEmail(sur.getEmail()))
                .thenReturn(false);
        Mockito.when(imageRepository.getById(1L))
                .thenReturn(new Image());
        Mockito.when(roleRepository.findByName(ERole.ROLE_ADMIN))
                .thenThrow(new RoleNotFoundException(ERole.ROLE_ADMIN));

        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/sugnup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(sur)))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testRefreshToken(){
        RefreshToken rt = new RefreshToken();
        rt.setToken("sometoken");

        Mockito.when(refreshTokenService.findByToken(rt.getToken()))
                .thenReturn(Optional.of(new RefreshToken()));

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/refreshtoken")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(rt)))
                    .andExpect(status().isOk());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testRefreshTokenNotFoundToken(){
        RefreshToken rt = new RefreshToken();
        rt.setToken("sometoken");

        Mockito.when(refreshTokenService.findByToken(rt.getToken()))
                .thenThrow(new TokenRefreshException("sometoken", ""));

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/refreshtoken")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString(rt)))
                    .andExpect(status().isNotFound());
        } catch (Exception ignored) {}
    }

    @Test
    public void testRefreshTokenUnauthorized(){
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/refreshtoken")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ConvertJsonToString.asJsonString("")))
                    .andExpect(status().isUnauthorized());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testLogoutUser(){
        User user = new User();
        user.setId(1L);
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenReturn(Optional.of(user));
        Mockito.when(refreshTokenService.deleteByUserId(user.getId()))
                .thenReturn(1);

        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/logout")
                            .with(csrf()))
                    .andExpect(status().isOk());
        }catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "stoyo", password = "default", authorities = "ROLE_ADMIN")
    public void testLogoutUserUsernameNotFound(){
        User user = new User();
        user.setId(1L);
        Mockito.when(userRepository.findByUsername("stoyo"))
                .thenThrow(new UserNotFoundException("stoyo"));

        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/logout")
                            .with(csrf()))
                    .andExpect(status().isNotFound());
        }catch (Exception ignored) {}
    }

    @Test
    public void testLogoutUserUnauthorized(){
        try{
            mockMvc.perform(MockMvcRequestBuilders
                            .post("api/auth/logout")
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }catch (Exception ignored) {}
    }
}
