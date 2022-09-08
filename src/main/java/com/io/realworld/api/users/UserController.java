package com.io.realworld.api.users;

import com.io.realworld.DTO.UserSignupRequest;
import com.io.realworld.DTO.UserResponse;
import com.io.realworld.repository.User;
import com.io.realworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public User signup(@Valid @RequestBody UserSignupRequest userSignupRequest){
        return userService.signup(userSignupRequest);
    }
}
