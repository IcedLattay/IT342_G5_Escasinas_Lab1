package com.escasinas.userAuth.controller;

import com.escasinas.userAuth.dto.ApiResponse;
import com.escasinas.userAuth.dto.LoginRequest;
import com.escasinas.userAuth.dto.LoginResponse;
import com.escasinas.userAuth.dto.RegisterRequest;
import com.escasinas.userAuth.model.User;
import com.escasinas.userAuth.security.JwtProvider;
import com.escasinas.userAuth.service.AuthService;
import com.escasinas.userAuth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    public AuthService authService;
    public UserService userService;
    public JwtProvider tokenProvider;

    public AuthController(
            AuthService authService,
            UserService userService,
            JwtProvider tokenProvider
    ) {
        this.authService = authService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User newUser = authService.createUser(request);

            String token = tokenProvider.generateToken(newUser);

            ApiResponse success = new ApiResponse(true, token);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Username is already taken.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Incorrect username or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/logout")
    public ApiResponse logout() {
        User user = userService.getCurrentUser();

        authService.logout(user.userId);

        return new ApiResponse(true, "Successfully logged out.");
    }
}
