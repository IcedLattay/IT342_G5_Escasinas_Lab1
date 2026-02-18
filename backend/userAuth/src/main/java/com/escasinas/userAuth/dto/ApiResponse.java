package com.escasinas.userAuth.dto;

public class ApiResponse {
    public Boolean success;
    public String message;

    public ApiResponse() {}

    public ApiResponse(
            Boolean success,
            String message
    ) {
        this.success = success;
        this.message = message;
    }
}
