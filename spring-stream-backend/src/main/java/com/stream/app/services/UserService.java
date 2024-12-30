package com.stream.app.services;

import com.stream.app.entities.User;

public interface UserService {
    User saveUser(User user);
    User getByEmail(String email);
    User getByName(String name);
    boolean userExist(String email);
}
