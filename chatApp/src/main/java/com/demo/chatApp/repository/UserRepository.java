package com.demo.chatApp.repository;

import com.demo.chatApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); //is useful for login logic.
    boolean existsByUsername(String username); //is helpful for validation during signup.
}
