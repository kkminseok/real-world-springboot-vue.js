package com.io.realworld.service;

import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import com.io.realworld.domain.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, jwtService);
    }


    @MethodSource("validUsers")
    @ParameterizedTest
    @DisplayName("회원가입_서비스_테스트")
    void signup(UserSignupRequest requestUser) {

        User user = User.builder()
                .username(requestUser.getUsername())
                .bio("")
                .email(requestUser.getEmail())
                .image("")
                .password("password")
                .build();

        doReturn(user).when(userRepository)
                .save(any(User.class));
        doReturn("token").when(jwtService).createToken(any(String.class));
        UserResponse savedUser = userService.signup(requestUser);


        // then
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getToken()).isNotNull();

        // verify
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }


    private UserSignupRequest getUserSignupRequest() {
        return UserSignupRequest.builder()
                .username("kms")
                .email("kms@gmail.com")
                .password("password")
                .build();
    }

    private UserResponse getUserResponse() {
        return UserResponse.builder()
                .username("kms")
                .email("kms@gmail.com")
                .bio("1")
                .image("image")
                .token("kms")
                .build();
    }

    public static Stream<Arguments> validUsers() {
        return Stream.of(
                Arguments.of(UserSignupRequest.builder().username("kms").email("kms@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("jyb").email("yyb95@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("kms").email("realWorld@gmail.com").password("password").build())
        );
    }

    private static Stream<Arguments> invalidUsers() {
        return Stream.of(
                Arguments.of(UserSignupRequest.builder().username("").email("kms@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("jyb").email("yyb95@gmail.com").password("").build()),
                Arguments.of(UserSignupRequest.builder().username("kms").email("").password("password").build())
        );
    }

}