package com.escasinas.userAuth.dto;

public class RegisterRequest {
    public String username;
    public String emailAddress;
    public String password;

    public RegisterRequest() {}

    public RegisterRequest(
            String username,
            String emailAddress,
            String password
    ) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
    }
}
