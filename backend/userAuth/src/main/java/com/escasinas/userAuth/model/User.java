package com.escasinas.userAuth.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long userId;

    public String username;

    public String displayName;

    public String emailAddress;

    public String password;

    public Boolean isActive;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;
}
