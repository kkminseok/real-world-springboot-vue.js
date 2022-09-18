package com.io.realworld.domain.aggregate.user.service;

import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.dto.UserSigninRequest;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.domain.aggregate.user.entity.User;

public interface UserService {
    UserResponse signup(UserSignupRequest userSignupRequest);

    UserResponse signin(UserSigninRequest userSigninRequest);

    UserResponse getCurrentUser(UserAuth userAuth);
}
