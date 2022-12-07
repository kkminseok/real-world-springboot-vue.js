package com.io.realworld.domain.aggregate.user.controller;

import com.io.realworld.domain.aggregate.user.dto.UserSigninRequest;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UserServiceImpl userService;

    public UsersController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse signup(@Valid @RequestBody  UserSignupRequest userSignupRequest) {
        return userService.signup(userSignupRequest);
    }

    @PostMapping(value = "/login")
    public UserResponse signin(@Valid @RequestBody UserSigninRequest userSigninRequest){
        return userService.signin(userSigninRequest);
    }
}
