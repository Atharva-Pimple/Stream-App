package com.stream.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stream.app.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
}
