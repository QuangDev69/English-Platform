package com.example.Platform.controller;

import com.example.Platform.dto.UserDTO;
import com.example.Platform.dto.UserLoginDTO;
import com.example.Platform.entity.User;
import com.example.Platform.middleware.JwtUtil;
import com.example.Platform.response.AuthenticationResponse;
import com.example.Platform.response.UserResponse;
import com.example.Platform.service.UserService;
import com.example.Platform.service.serviceImpl.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user")
public class UserController {
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }
        try {
            User createdUser = userService.createUser(userDTO);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        String loginIdentifier = userLoginDTO.getUsername() != null ? userLoginDTO.getUsername() : userLoginDTO.getPhoneNumber();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginIdentifier, userLoginDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Incorrect username, phone number, or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginIdentifier);
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }



    @GetMapping("/{userId}/details")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getUserDetail(@RequestHeader("Authorization") String token){
        try {
            String extractToken = token.substring(7);
            UserResponse user = userService.getUserDetail(extractToken);
            return ResponseEntity.ok(user);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }
}
