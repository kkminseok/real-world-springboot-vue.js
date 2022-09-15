package com.io.realworld.domain.aggregate.user.service;

import com.io.realworld.domain.aggregate.user.dto.UserSigninRequest;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.domain.aggregate.user.entity.User;

public interface UserService {
    User signup(UserSignupRequest userSignupRequest);

    User signin(UserSigninRequest userSigninRequest);
}
