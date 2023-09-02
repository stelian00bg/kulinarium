package com.group15.TUKulinarium.controllers;

import com.group15.TUKulinarium.exception.TokenRefreshException;
import com.group15.TUKulinarium.exception.UserNotFoundException;
import com.group15.TUKulinarium.models.ERole;
import com.group15.TUKulinarium.models.RefreshToken;
import com.group15.TUKulinarium.models.Role;
import com.group15.TUKulinarium.models.User;
import com.group15.TUKulinarium.payload.request.LoginRequest;
import com.group15.TUKulinarium.payload.request.SignupRequest;
import com.group15.TUKulinarium.payload.request.TokenRefreshRequest;
import com.group15.TUKulinarium.payload.response.JwtResponse;
import com.group15.TUKulinarium.payload.response.MessageResponse;
import com.group15.TUKulinarium.payload.response.TokenRefreshResponse;
import com.group15.TUKulinarium.repository.ImageRepository;
import com.group15.TUKulinarium.repository.RoleRepository;
import com.group15.TUKulinarium.repository.UserRepository;
import com.group15.TUKulinarium.security.jwt.JwtUtils;
import com.group15.TUKulinarium.security.services.RefreshTokenService;
import com.group15.TUKulinarium.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    ImageRepository imageRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail());
        user.setImage(imageRepository.getById(1L));
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.ROLE_USER).
                orElseThrow(() -> new RuntimeException("Error: Role not found"));
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(Authentication auth) {
        String username = auth.getName();
        Long userId = userRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException(username)).
                getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful"));
    }
}
