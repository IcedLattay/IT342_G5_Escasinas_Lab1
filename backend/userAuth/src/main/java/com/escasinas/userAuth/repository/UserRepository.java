package com.escasinas.userAuth.repository;

import com.escasinas.userAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Boolean existsByUsername(String username);
    public Optional<User> findByUsername(String username);
    public Boolean existsByEmailAddress(String emailAddress);
    public Optional<User> findByEmailAddress(String emailAddress);
}
