package com.io.realworld.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("회원가입 레포 테스트")
    void save() {

        //given
        User user = user();

        //when
        User saved_user = userRepository.save(user);

        //then
        assertThat(saved_user.getUsername()).isEqualTo(user.getUsername());
        assertThat(saved_user.getEmail()).isEqualTo(user.getEmail());
        assertThat(saved_user.getBio()).isEqualTo(user.getBio());
        assertThat(saved_user.getPassword()).isEqualTo(user.getPassword());
        assertThat(saved_user.getImage()).isEqualTo(user.getImage());
    }


    private User user() {
        return User.builder()
                .username("kms")
                .email("kms@gamil.com")
                .bio("1")
                .image("image")
                .build();
    }
}