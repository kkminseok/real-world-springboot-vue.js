package com.io.realworld.service;

import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.dto.UserSigninRequest;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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


    @MethodSource("duplicateUsers")
    @ParameterizedTest
    @DisplayName("회원가입_중복_서비스_테스트")
    void duplicateSignup(UserSignupRequest requestUser) {
        User user = User.builder()
                .username(requestUser.getUsername())
                .bio("")
                .email(requestUser.getEmail())
                .image("")
                .password(requestUser.getPassword())
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(null).thenThrow(new CustomException(Error.DUPLICATE_USER));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.signup(requestUser);
    }

    @MethodSource("validLoginUsers")
    @ParameterizedTest
    @DisplayName("로그인 성공 서비스 테스트")
    void loginSuccess(UserSigninRequest userSigninRequest){
        User user = User.builder()
                .bio("")
                .email(userSigninRequest.getEmail())
                .image("")
                .password(userSigninRequest.getPassword())
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);
        userService.signin(userSigninRequest);
    }

    @MethodSource("invalidLoginUsers")
    @ParameterizedTest
    @DisplayName("로그인 실패 서비스 테스트")
    void loginFail(UserSigninRequest userSigninRequest){

        when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        try {
            userService.signin(userSigninRequest);
        }catch (CustomException e){
            assertThat(e.getError().equals(Error.EMAIL_NULL_OR_INVALID));
            assertThat(e.getError().getMessage().equals(Error.EMAIL_NULL_OR_INVALID.getMessage()));
        }
    }


    public static Stream<Arguments> validUsers() {
        return Stream.of(
                Arguments.of(UserSignupRequest.builder().username("kms").email("kms@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("jyb").email("yyb95@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("kms").email("realWorld@gmail.com").password("password").build())
        );
    }

    private static Stream<Arguments> duplicateUsers() {
        return Stream.of(
                Arguments.of(UserSignupRequest.builder().username("kms").email("kms@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("kms").email("kms@gmail.com").password("password").build())
        );
    }

    private static Stream<Arguments> validLoginUsers(){
        return Stream.of(
                Arguments.of(UserSigninRequest.builder().email("kms@naver.com").password("password").build()),
                Arguments.of(UserSigninRequest.builder().email("jjy@gmail.com").password("password").build())
        );
    }

    private static Stream<Arguments> invalidLoginUsers(){
        return Stream.of(
                Arguments.of(UserSigninRequest.builder().email("invalidEmail").password("password").build())
        );
    }

}