package com.io.realworld.domain.aggregate.profile.repository;

import com.io.realworld.domain.aggregate.profile.entity.Follow;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
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

    @MethodSource("getFolloweeAndFollower")
    @ParameterizedTest(name = "repo:팔로잉 저장 테스트")
    void save_follow(User followee, User follower){

        userRepository.save(followee);
        userRepository.save(follower);
        Follow follow = Follow.builder().id(1L).followee(followee).follower(follower).build();
        //when
        Follow getFollow = profileRepository.save(follow);

        //then
        assertThat(getFollow.getFollowee()).isEqualTo(followee);
        assertThat(getFollow.getFollower()).isEqualTo(follower);
    }

    @MethodSource("getFolloweeAndFollower")
    @ParameterizedTest(name = "repo:팔로잉대상이 없을때")
    void findByFolloweeIdAndFollowerId(User followee, User follower) {

        //when
        Optional<Follow> follow = profileRepository.findByFolloweeIdAndFollowerId(followee.getId(),follower.getId());

        //then
        assertThat(follow).isEmpty();
    }


    @MethodSource("getFolloweeAndFollower")
    @ParameterizedTest(name = "repo:팔로잉대상 있을때")
    void findByFolloweeIdAndFollowerId_already_exits(User followee, User follower) {
        //given
        userRepository.save(followee);
        userRepository.save(follower);
        Follow follow = Follow.builder().id(1L).followee(followee).follower(follower).build();
        //when
        profileRepository.save(follow);
        Optional<Follow> getFollow = profileRepository.findByFolloweeIdAndFollowerId(followee.getId(),follower.getId());

        //then
        assertThat(getFollow).isNotNull();
        assertThat(getFollow.get().getFollowee()).isEqualTo(followee);
        assertThat(getFollow.get().getFollower()).isEqualTo(follower);
    }

    @MethodSource("getFolloweeAndFollower")
    @ParameterizedTest(name = "repo:팔로잉 제거 테스터")
    void deleteFollow(User followee, User follower){
        //given
        userRepository.save(followee);
        userRepository.save(follower);
        Follow follow = Follow.builder().followee(followee).follower(follower).build();
        //when
        profileRepository.save(follow);
        Optional<Follow> getFollow = profileRepository.findByFolloweeIdAndFollowerId(followee.getId(),follower.getId());
        profileRepository.delete(getFollow.get());
        //then
    }

    @MethodSource("getFolloweeAndFollower")
    @ParameterizedTest(name = "repo:피드 테스트")
    void feedArticle(User followee, User follower){
        User follower2 = User.builder()
                .bio("follower bio")
                .email("follower2@email.com")
                .password("password")
                .image("follower image")
                .username("follower2")
                .build();
        userRepository.save(followee);
        userRepository.save(follower);
        userRepository.save(follower2);

        UserAuth userAuth = UserAuth.builder().id(followee.getId()).username("username").bio("bio").email("email").build();

        Follow follow = Follow.builder().followee(followee).follower(follower).build();
        Follow follow2 = Follow.builder().followee(followee).follower(follower2).build();

        profileRepository.save(follow);
        profileRepository.save(follow2);

        List<Follow> follows = profileRepository.findByFolloweeId(userAuth.getId());

        assertThat(follows.get(0).getFollowee().getUsername()).isEqualTo(followee.getUsername());
        assertThat(follows.get(0).getFollower().getUsername()).isEqualTo(follower.getUsername());

        assertThat(follows.get(1).getFollowee().getUsername()).isEqualTo(followee.getUsername());
        assertThat(follows.get(1).getFollower().getUsername()).isEqualTo(follower2.getUsername());
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