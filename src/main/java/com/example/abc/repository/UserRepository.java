package com.example.abc.repository;

import com.example.abc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findUserByUsername(String username);
}
