package com.io.realworld.domain.aggregate.user.controller;

import com.io.realworld.domain.aggregate.user.dto.UserSigninRequest;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UsersController {


    private final UserServiceImpl userService;


    public UsersController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @PostMapping(value = "")
    public UserResponse signup(@Valid @RequestBody  UserSignupRequest userSignupRequest) {
        return userService.signup(userSignupRequest);
    }

    @PostMapping(value = "/login")
    public UserResponse signin(@Valid @RequestBody UserSigninRequest userSigninRequest){
        return userService.signin(userSigninRequest);
    }
}
