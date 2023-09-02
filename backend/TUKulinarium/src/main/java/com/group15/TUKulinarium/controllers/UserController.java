package com.group15.TUKulinarium.controllers;

import com.group15.TUKulinarium.exception.*;
import com.group15.TUKulinarium.models.ERole;
import com.group15.TUKulinarium.models.User;
import com.group15.TUKulinarium.payload.request.ChangeProfilePictureRequest;
import com.group15.TUKulinarium.payload.request.UpdateEmailRequest;
import com.group15.TUKulinarium.payload.request.UpdatePasswordRequest;
import com.group15.TUKulinarium.payload.response.SimpleUserResponse;
import com.group15.TUKulinarium.repository.ImageRepository;
import com.group15.TUKulinarium.repository.RoleRepository;
import com.group15.TUKulinarium.repository.UserRepository;
import com.group15.TUKulinarium.service.ImageWriter;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageWriter imageWriter;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new SimpleUserResponse(userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("user with id: %d, can't be found", id))
        )));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> search(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "perPage", defaultValue = "5") int perPage,
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "email", required = false, defaultValue = "") String email) {

        Pageable pageable = PageRequest.of(currentPage-1, perPage);
        Page<User> users = userRepository.findPagedUsers(pageable, username.toLowerCase(), name.toLowerCase(), email.toLowerCase());
        if (users.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        List<SimpleUserResponse> userResponseList = new LinkedList<>();
        users.forEach(user -> {
            userResponseList.add(new SimpleUserResponse(user));
        });
        return ResponseEntity.ok(userResponseList);
    }

    @PutMapping(value = "/makeAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> makeAdmin(@RequestParam(value = "username") String username, Authentication auth) {
        boolean isUpdate = false;
        var currUser = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new UserNotFoundException(auth.getName()));
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        for(var role : currUser.getRoles()){
            if(role.getName() == ERole.ROLE_ADMIN){
                user.getRoles().add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(
                        () -> new RoleNotFoundException(ERole.ROLE_ADMIN)
                ));
                userRepository.save(user);
                return ResponseEntity.ok(String.format("User with username: %s, is now admin", user.getUsername()));
            }
        }

        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Can't make that user an admin unless you are admin");
    }

    @PutMapping(value = "/updateProfilePicture")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProfilePicture(@Valid @ModelAttribute ChangeProfilePictureRequest request, Authentication auth) throws ImageWriterException {
        var user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new UserNotFoundException(auth.getName())
        );
        var userImage = user.getImage();
        userImage.setLink(imageWriter.WriteImage(request.getPicture()));
        imageRepository.save(userImage);
        return ResponseEntity.ok("profile image updated successfully");
    }

    @PutMapping(value = "/updateName")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateName(@RequestParam(value = "newName") String newName, Authentication auth) {
        var user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new UserNotFoundException(auth.getName())
        );
        user.setName(newName);
        userRepository.save(user);
        return ResponseEntity.ok(String.format("profile name updated successfully with: %s", newName));
    }

    @PutMapping(value = "/updateUsername")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUsername(@RequestParam(value = "newUsername") String newUsername, Authentication auth) {
        var user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new UserNotFoundException(auth.getName())
        );
        if (userRepository.existsByUsername(newUsername)){
            throw new UsernameAlreadyExistsException(newUsername);
        }
        user.setUsername(newUsername);
        userRepository.save(user);
        return ResponseEntity.ok(String.format("profile username updated successfully with: %s, refresh your token!", newUsername));
    }

    @PutMapping(value = "/updateEmail")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateEmail(@Valid @RequestBody UpdateEmailRequest request, Authentication auth) {
        var user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new UserNotFoundException(auth.getName())
        );
        if (userRepository.existsByEmail(request.getNewEmail())){
            throw new UsernameAlreadyExistsException(request.getNewEmail());
        }
        user.setEmail(request.getNewEmail());
        userRepository.save(user);
        return ResponseEntity.ok(String.format("profile email updated successfully with: %s", request.getNewEmail()));
    }

    @PutMapping(value = "/updatePassword")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest request, Authentication auth) {
        //This is done to check the password, because hashing algorithm always generate different hashes
        //with same password input, because random salt is added in the end of the string before attempting to hash
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(auth.getName(), request.getCurrentPassword()));

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new PasswordMismatchException();
        }
        var user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new UserNotFoundException(auth.getName())
        );

        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Password updated successfully");
    }
}
