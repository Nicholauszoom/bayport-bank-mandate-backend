package com.example.Project.Management.bayport.auth.user.service;

import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getByUserName(String upperCase) {
        return getByUserName(upperCase);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserId(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}
