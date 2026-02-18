package com.escasinas.userAuth.controller;

import com.escasinas.userAuth.dto.UserResponse;
import com.escasinas.userAuth.model.User;
import com.escasinas.userAuth.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    public UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse me() {
        User user = userService.getCurrentUser();

        return new UserResponse(
                user.userId,
                user.username,
                user.emailAddress
        );
    }
}
