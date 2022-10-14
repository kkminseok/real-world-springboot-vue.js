package com.io.realworld.domain.aggregate.profile.service;

import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.entity.Follow;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.profile.repository.*;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mockito.internal.matchers.Null;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;


    @Override
    public ProfileResponse getProfile(UserAuth userAuth, String username) {
        Optional<User> wantFindUser = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(() -> new CustomException(Error.USER_NOT_FOUND)));
        Boolean followStatus = profileRepository.findByFolloweeIdAndFollowerId(userAuth.getId(), wantFindUser.get().getId()).isPresent();

        return convertProfile(followStatus, wantFindUser);
    }

    @Override
    public ProfileResponse followUser(UserAuth userAuth, String username) {
        Optional<User> wantFindUser = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(() -> new CustomException(Error.USER_NOT_FOUND)));
        Optional<User> currentUser = userRepository.findById(userAuth.getId());
        Follow followPair = Follow.builder().followee(wantFindUser.get()).follower(currentUser.get()).build();
        Boolean followStatus = profileRepository.save(followPair).getFollowee().equals(wantFindUser.get());
        return convertProfile(followStatus,wantFindUser);
    }


    private ProfileResponse convertProfile(Boolean followStatus, Optional<User> user) {
        return ProfileResponse.builder().
                username(user.get().getUsername()).
                bio(user.get().getBio()).
                image(user.get().getImage()).
                following(followStatus).build();
    }
}
