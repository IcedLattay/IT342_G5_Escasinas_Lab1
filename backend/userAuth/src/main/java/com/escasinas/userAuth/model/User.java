package com.escasinas.userAuth.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long userId;

    public String username;

    @Column(name = "display_name")
    public String displayName;

    @Column(name = "email_address")
    public String emailAddress;

    public String password;

    @Column(name = "is_active")
    public Boolean isActive;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
}
