package com.io.realworld.domain.aggregate.user.controller;

import com.io.realworld.domain.aggregate.user.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    public User currentUser(HttpServletRequest request){
        System.out.println("request = " + request);
        return null;
    }
}
