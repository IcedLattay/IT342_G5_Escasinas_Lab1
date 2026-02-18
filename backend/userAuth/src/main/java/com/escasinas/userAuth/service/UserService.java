package com.escasinas.userAuth.service;

import com.escasinas.userAuth.model.User;
import com.escasinas.userAuth.repository.UserRepository;
import com.escasinas.userAuth.security.JwtProvider;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    public UserRepository userRepository;
    public JwtProvider tokenProvider;

    public UserService(
            UserRepository userRepository,
            JwtProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No user is currently logged in!");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
