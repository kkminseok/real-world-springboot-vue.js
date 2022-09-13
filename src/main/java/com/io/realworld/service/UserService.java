package com.io.realworld.service;

import com.io.realworld.DTO.UserSigninRequest;
import com.io.realworld.DTO.UserSignupRequest;
import com.io.realworld.repository.User;

public interface UserService {
    User signup(UserSignupRequest userSignupRequest);

    User signin(UserSigninRequest userSigninRequest);
}
