package com.io.realworld.domain.aggregate.user.service;

import com.io.realworld.domain.aggregate.user.dto.*;

public interface UserService {
    UserResponse signup(UserSignupRequest userSignupRequest);

    UserResponse signin(UserSigninRequest userSigninRequest);

    UserResponse getCurrentUser(UserAuth userAuth);

    UserResponse updateUser(UserUpdate userUpdate, UserAuth userAuth);
}
