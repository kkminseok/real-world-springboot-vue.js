package com.io.realworld.repository;

import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import org.hibernate.annotations.Filter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @MethodSource("validUsers")
    @ParameterizedTest(name = "repo:회원가입 성공 테스트")
    void save(User user) {

        //when
        User saved_user = userRepository.save(user);

        //then
        assertThat(saved_user.getUsername()).isEqualTo(user.getUsername());
        assertThat(saved_user.getEmail()).isEqualTo(user.getEmail());
        assertThat(saved_user.getBio()).isEqualTo(user.getBio());
        assertThat(saved_user.getPassword()).isEqualTo(user.getPassword());
        assertThat(saved_user.getImage()).isEqualTo(user.getImage());
    }

    public static Stream<Arguments> validUsers() {
        return Stream.of(Arguments.of(User.builder().username("kms").password("").email("kms@gamil.com").bio("1").image("image").build()));
    }
}