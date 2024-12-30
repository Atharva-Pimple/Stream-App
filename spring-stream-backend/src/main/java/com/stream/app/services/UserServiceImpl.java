package com.stream.app.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stream.app.entities.User;
import com.stream.app.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
    }

    @Override
    public User getByName(String name) {
        return userRepository.findByName(name).orElseThrow(()->new RuntimeException("User not found"));
    }

    @Override
    public boolean userExist(String email) {
        User user=userRepository.findByEmail(email).orElse(null);
        return user!=null;
    }

}
