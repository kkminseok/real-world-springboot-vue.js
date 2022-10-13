package com.io.realworld.domain.aggregate.profile.service;

import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;

public interface ProfileService {
    ProfileResponse getProfile(UserAuth userAuth, String username);

}
