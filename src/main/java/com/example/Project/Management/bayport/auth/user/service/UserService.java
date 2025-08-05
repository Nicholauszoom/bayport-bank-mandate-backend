package com.example.Project.Management.bayport.auth.user.service;

import com.example.Project.Management.bayport.auth.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getByUserName(String upperCase);

    User saveUser(User user);


    Optional<User> findByUserId(long userId);

    List<User> getAllUsers();

    List<User> findAll();
}
