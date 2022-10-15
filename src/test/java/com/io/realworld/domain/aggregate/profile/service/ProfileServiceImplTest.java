package com.io.realworld.domain.aggregate.profile.service;

import com.io.realworld.domain.aggregate.profile.entity.Follow;
import com.io.realworld.domain.aggregate.profile.repository.ProfileRepository;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.dto.UserUpdate;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("프로필조회 팔로워 못 찾을때")
    void getProfile_userNotFound() {
        UserAuth userAuth = UserAuth.builder().id(1L).build();
        when(userRepository.findByUsername(any(String.class))).thenThrow(new CustomException(Error.USER_NOT_FOUND));
        try{
            profileService.getProfile(userAuth,"username");
        }catch (CustomException e){
            assertThat(e.getError().equals(Error.USER_NOT_FOUND));
            assertThat(e.getError().getMessage().equals(Error.USER_NOT_FOUND.getMessage()));
        }
    }

    @ParameterizedTest
    @MethodSource("returnUserObject")
    @DisplayName("프로필조회 성공")
    void getProfile_Success(User user) {
        UserAuth userAuth = UserAuth.builder().id(1L).build();
        Follow follow = Follow.builder().build();
        when(userRepository.findByUsername(any(String.class))).thenReturn(ofNullable(user));
        when(profileRepository.findByFolloweeIdAndFollowerId(eq(userAuth.getId()),eq(user.getId()))).thenReturn(ofNullable(follow));
        profileService.getProfile(userAuth,"username");
    }

    @Test
    @DisplayName("팔로워 실패 대상을 못 찾음")
    void followUser_Fail_userNotFound() {
        UserAuth userAuth = UserAuth.builder().id(1L).build();
        when(userRepository.findByUsername(any(String.class))).thenThrow(new CustomException(Error.USER_NOT_FOUND));
        try{
            profileService.followUser(userAuth,"username");
        }catch (CustomException e){
            assertThat(e.getError().equals(Error.USER_NOT_FOUND));
            assertThat(e.getError().getMessage().equals(Error.USER_NOT_FOUND.getMessage()));
        }
    }

    @ParameterizedTest
    @MethodSource("returnUserObject")
    @DisplayName("팔로워 실패 중복 이미 팔로워 대상인 경우")
    void followUser_Fail_DuplicateFollow(User user){
        UserAuth userAuth = UserAuth.builder().id(1L).build();
        User authUserInRepo = User.builder().id(userAuth.getId()).build();
        when(userRepository.findByUsername(eq(user.getUsername()))).thenReturn(ofNullable(user));
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(ofNullable(authUserInRepo));
        when(profileRepository.findByFolloweeIdAndFollowerId(eq(authUserInRepo.getId()),eq(user.getId()))).thenThrow(new CustomException(Error.ALREADY_FOLLOW));

        try{
            profileService.followUser(userAuth,user.getUsername());
        }catch (CustomException e){
            assertThat(e.getError().equals(Error.ALREADY_FOLLOW));
            assertThat(e.getError().getMessage().equals(Error.ALREADY_FOLLOW.getMessage()));
        }
    }

    @ParameterizedTest
    @MethodSource("returnUserObject")
    @DisplayName("팔로워 성공")
    void followUser_Success(User user){
        UserAuth userAuth = UserAuth.builder().id(1L).build();
        User authUserInRepo = User.builder().id(userAuth.getId()).build();
        Follow follow = Follow.builder().followee(authUserInRepo).build();
        when(userRepository.findByUsername(eq(user.getUsername()))).thenReturn(ofNullable(user));
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(ofNullable(authUserInRepo));
        when(profileRepository.save(any(Follow.class))).thenReturn(follow);

        profileService.followUser(userAuth,user.getUsername());
    }



    private static Stream<String> foundNotUsers(){
        return Stream.of("username1","username2","username3");
    }

    private static Stream<Arguments> returnUserObject(){
        return Stream.of(Arguments.of(User.builder().id(100L).username("username").bio("bio").password("password").image("image").build()));

    }
}