package com.io.realworld.api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import com.io.realworld.domain.aggregate.user.controller.UsersController;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(controllers = UsersController.class)
class UsersControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    private UsersController usersController;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtService jwtService;


    @WithMockUser
    @MethodSource("validUsers")
    @ParameterizedTest
    @DisplayName("회원가입 컨트롤러 테스트")
    void signup(UserSignupRequest userSignupRequest) throws Exception {

        User result = User.builder().username(userSignupRequest.getUsername()).password(userSignupRequest.getPassword()).email(userSignupRequest.getEmail()).build();
        Mockito.doReturn(result).when(userService)
                .signup(any(UserSignupRequest.class));

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
    @ParameterizedTest
    @DisplayName("회원가입 중복 컨트롤러 테스트")
    void signupDuplicate(UserSignupRequest user) throws Exception{
        Mockito.doThrow(new CustomException(Error.DUPLICATE_USER)).when(userService).signup(any(UserSignupRequest.class));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .with(csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.message",Matchers.equalTo(Error.DUPLICATE_USER.getMessage())));
    }

    @WithMockUser
    @MethodSource("invalidUsers")
    @ParameterizedTest
    @DisplayName("회원가입 빈값 컨트롤러 테스트")
    void signupBlankData(UserSignupRequest user) throws Exception{
        Mockito.doThrow(new CustomException(Error.SIGNUP_NULL_DATA)).when(userService).signup(any(UserSignupRequest.class));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .with(csrf())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
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