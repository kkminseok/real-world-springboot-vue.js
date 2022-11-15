package com.io.realworld.domain.aggregate.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.dto.UserSigninRequest;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(controllers = UsersController.class)
class UsersControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UsersController usersController;

    @MockBean
    private UserServiceImpl userService;

    //because JwtFilter
    @MockBean
    private JwtService jwtService;


    @WithMockUser
    @MethodSource("validUsers")
    @ParameterizedTest(name = "controller:회원가입 성공 테스트")
    void signup(UserSignupRequest userSignupRequest) throws Exception {
        UserResponse result = UserResponse.builder()
                .username(userSignupRequest.getUsername())
                .email(userSignupRequest.getEmail())
                .build();
        when(userService.signup(any(UserSignupRequest.class))).thenReturn(result);

        //when , then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupRequest))
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(userSignupRequest.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.equalTo(userSignupRequest.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username", Matchers.is(userSignupRequest.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username", Matchers.equalTo(userSignupRequest.getUsername())));
    }

    @WithMockUser
    @MethodSource("validUsers")
    @ParameterizedTest(name = "conroller:회원가입 중복 테스트")
    void signupDuplicate(UserSignupRequest user) throws Exception {
        when(userService.signup(any(UserSignupRequest.class))).thenThrow(new CustomException(Error.DUPLICATE_EMAIL));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.message", Matchers.equalTo(Error.DUPLICATE_EMAIL.getMessage())));
    }

    @WithMockUser
    @MethodSource("invalidUsers")
    @ParameterizedTest(name = "controller:회원가입 빈값 테스트")
    void signupBlankData(UserSignupRequest user) throws Exception {
        when(userService.signup(any(UserSignupRequest.class))).thenThrow(new CustomException(Error.SIGNUP_NULL_DATA));
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.message", Matchers.equalTo(Error.SIGNUP_NULL_DATA.getMessage())));
    }

    @WithMockUser
    @MethodSource("validLoginUsers")
    @ParameterizedTest(name = "controller:로그인 성공 테스트")
    void signInSuccess(UserSigninRequest loginUser) throws Exception {
        UserResponse user = UserResponse.builder()
                        .username("")
                        .image("")
                        .token("mytoken")
                        .email(loginUser.getEmail())
                        .bio("").build();
        when(userService.signin(any(UserSigninRequest.class))).thenReturn(user);

        mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser))
                        .with(csrf())
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(loginUser.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.equalTo(loginUser.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.user.token", Matchers.notNullValue()));
    }

    @WithMockUser
    @MethodSource("invalidLoginUsers")
    @ParameterizedTest(name = "controller:로그인 실패 테스트")
    void signInFail(UserSigninRequest loginUser) throws Exception {
        when(userService.signin(any(UserSigninRequest.class))).thenThrow(new CustomException(Error.EMAIL_NULL_OR_INVALID));

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginUser))
                                .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.message",Matchers.equalTo(Error.EMAIL_NULL_OR_INVALID.getMessage())));
    }


    private static Stream<Arguments> validUsers() {
        return Stream.of(
                Arguments.of(UserSignupRequest.builder().username("kms").email("kms@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("jyb").email("yyb95@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("kms").email("realWorld@gmail.com").password("password").build())
        );
    }

    private static Stream<Arguments> invalidUsers() {
        return Stream.of(
                Arguments.of(UserSignupRequest.builder().username("hi").email("").password("password").build())
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
                Arguments.of(UserSigninRequest.builder().email("").password("password").build())
        );
    }

}