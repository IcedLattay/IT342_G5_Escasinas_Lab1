package com.escasinas.userAuth.service;

import com.escasinas.userAuth.dto.ApiResponse;
import com.escasinas.userAuth.dto.LoginRequest;
import com.escasinas.userAuth.dto.LoginResponse;
import com.escasinas.userAuth.dto.RegisterRequest;
import com.escasinas.userAuth.model.User;
import com.escasinas.userAuth.repository.UserRepository;
import com.escasinas.userAuth.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    public UserRepository userRepository;
    public PasswordEncoder passwordEncoder;
    public JwtProvider tokenProvider;


    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public User createUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken.");
        }

        User newUser = new User();

        System.out.println("Email from request: " + request.emailAddress);
        newUser.username = request.username;
        newUser.emailAddress = request.emailAddress;
        newUser.password = passwordEncoder.encode(request.password);

        return userRepository.save(newUser);
    }

    public LoginResponse authenticate(LoginRequest request) {
        User user = userRepository.findByUsername(request.username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password."));

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password.");
        }

        String token = tokenProvider.generateToken(user);

        return new LoginResponse(token);
    }

    public void logout(Long userId) {

    }


}
