package com.io.realworld.api.users;

import com.io.realworld.DTO.UserSignupRequest;
import com.io.realworld.DTO.UserResponse;
import com.io.realworld.repository.User;
import com.io.realworld.service.UserService;
import com.io.realworld.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {


    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public UserResponse signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        User user = userService.signup(userSignupRequest);
        log.info("register");

        return UserResponse.builder().username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }
}
