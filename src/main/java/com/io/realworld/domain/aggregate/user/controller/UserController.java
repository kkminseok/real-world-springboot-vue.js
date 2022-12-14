package com.io.realworld.domain.aggregate.user.controller;

import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.domain.aggregate.user.dto.UserUpdate;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.service.UserService;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserResponse currentUser(@AuthenticationPrincipal UserAuth userAuth) {
        return userService.getCurrentUser(userAuth);
    }

    @PutMapping
    public UserResponse updateUser(@Valid @RequestBody UserUpdate userUpdate, @AuthenticationPrincipal UserAuth userAuth) {
        return userService.updateUser(userUpdate, userAuth);
    }
}
