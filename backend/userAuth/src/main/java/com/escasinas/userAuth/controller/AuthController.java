package com.escasinas.userAuth.controller;

import com.escasinas.userAuth.dto.ApiResponse;
import com.escasinas.userAuth.dto.LoginRequest;
import com.escasinas.userAuth.dto.LoginResponse;
import com.escasinas.userAuth.dto.RegisterRequest;
import com.escasinas.userAuth.model.User;
import com.escasinas.userAuth.service.AuthService;
import com.escasinas.userAuth.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    public AuthService authService;
    public UserService userService;

    public AuthController(
            AuthService authService,
            UserService userService
    ) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse register(@RequestBody RegisterRequest request) {
        authService.createUser(request);

        LoginRequest loginRequest = new LoginRequest(request.username, request.password);

        String token = authService.authenticate(loginRequest).token;

        return new ApiResponse(true, token);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/logout")
    public ApiResponse logout() {
        User user = userService.getCurrentUser();

        authService.logout(user.userId);

        return new ApiResponse(true, "Successfully logged out.");
    }
}
