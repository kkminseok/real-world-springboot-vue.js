package com.io.realworld.domain.aggregate.profile.service;

import com.io.realworld.domain.aggregate.user.dto.UserAuth;

public interface ProfileService {
    String getProfile(UserAuth userAuth, String username);

}
