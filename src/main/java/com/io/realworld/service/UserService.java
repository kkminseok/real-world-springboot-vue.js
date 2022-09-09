package com.io.realworld.service;

import com.io.realworld.DTO.UserSignupRequest;
import com.io.realworld.repository.User;
import com.io.realworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User signup(UserSignupRequest userSignupRequest) {
        return userRepository.save(User.of(userSignupRequest.getUsername(),
                userSignupRequest.getEmail(),
                userSignupRequest.getPassword()));
    }

}
