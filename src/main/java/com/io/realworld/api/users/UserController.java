package com.io.realworld.api.users;

import com.io.realworld.DTO.UserSigninRequest;
import com.io.realworld.DTO.UserSignupRequest;
import com.io.realworld.DTO.UserResponse;
import com.io.realworld.repository.User;
import com.io.realworld.service.JwtService;
import com.io.realworld.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {


    private final UserServiceImpl userService;

    private final JwtService jwtService;


    public UserController(UserServiceImpl userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @PostMapping(value = "")
    public UserResponse signup(@Valid @RequestBody  UserSignupRequest userSignupRequest) {
        User user = userService.signup(userSignupRequest);
        return UserResponse.builder().username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .token(jwtService.createToken(user.getEmail()))
                .build();
    }

    @PostMapping(value = "/login")
    public UserResponse signin(@Valid @RequestBody UserSigninRequest userSigninRequest){
        User user = userService.signin(userSigninRequest);

        return UserResponse.builder().username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .token(jwtService.createToken(user.getEmail()))
                .build();
    }
}
