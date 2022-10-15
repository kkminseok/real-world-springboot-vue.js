package com.io.realworld.domain.aggregate.profile.repository;

import com.io.realworld.domain.aggregate.profile.entity.Follow;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProfileRepositoryTest {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @DisplayName("팔로잉 저장 테스트")
    @MethodSource("getFolloweeAndFollower")
    @ParameterizedTest(name = "{index} => followee={0}, follower={1}")
    void save_follow(User followee, User follower){

        userRepository.save(followee);
        userRepository.save(follower);
        Follow follow = Follow.builder().followee(followee).follower(follower).build();
        //when
        Follow getFollow = profileRepository.save(follow);

        //then
        assertThat(getFollow.getFollowee()).isEqualTo(followee);
        assertThat(getFollow.getFollower()).isEqualTo(follower);
    }

    @DisplayName("팔로잉대상 없을때")
    @MethodSource("getFolloweeAndFollower")
    @ParameterizedTest(name = "{index} => followee={0}, follower={1}")
    void findByFolloweeIdAndFollowerId(User followee, User follower) {

        //when
        Optional<Follow> follow = profileRepository.findByFolloweeIdAndFollowerId(followee.getId(),follower.getId());

        //then
        assertThat(follow).isEmpty();
    }


    @DisplayName("팔로잉대상 있때")
    @MethodSource("getFolloweeAndFollower")
    @ParameterizedTest(name = "{index} => followee={0}, follower={1}")
    void findByFolloweeIdAndFollowerId_already_exits(User followee, User follower) {
        userRepository.save(followee);
        userRepository.save(follower);
        Follow follow = Follow.builder().followee(followee).follower(follower).build();
        //when
        profileRepository.save(follow);
        Optional<Follow> getFollow = profileRepository.findByFolloweeIdAndFollowerId(followee.getId(),follower.getId());

        //then
        assertThat(getFollow).isNotNull();
        assertThat(getFollow.get().getFollowee()).isEqualTo(followee);
        assertThat(getFollow.get().getFollower()).isEqualTo(follower);
    }

    private static Stream<Arguments> getFolloweeAndFollower(){
        return Stream.of(
                Arguments.of(
                User.builder()
                        .bio("follow bio")
                        .email("follow@email.com")
                        .password("password")
                        .image("follow image")
                        .username("follow")
                        .build(),
                User.builder()
                        .bio("follower bio")
                        .email("follower@email.com")
                        .password("password")
                        .image("follower image")
                        .username("follower")
                        .build())
                );
    }
}