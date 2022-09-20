package com.io.realworld.domain.aggregate.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworld.config.WithAuthUser;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.service.UserServiceDetail;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.security.WebConfig;
import com.io.realworld.security.jwt.JwtAuthenticationFilter;
import com.io.realworld.security.jwt.JwtConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    JwtService jwtService;


    @WithAuthUser(email = "test@gmail.com",username = "kms",id = 1L)
    @Test
    @DisplayName("현재유저 찾기 컨트롤러 테스트")
    void currentUserSuccess() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .username("kms")
                .email("test@gmail.com")
                .build();
        when(userService.getCurrentUser(any(UserAuth.class))).thenReturn(userResponse);
        mockMvc.perform(get("/api/user")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", Matchers.equalTo(userResponse.getEmail())))
                .andExpect(jsonPath("$.user.bio", Matchers.equalTo(userResponse.getBio())))
                .andExpect(jsonPath("$.user.username", Matchers.equalTo(userResponse.getUsername())))
                .andExpect(jsonPath("$.user.image", Matchers.equalTo(userResponse.getImage())));
    }

    private static Stream<Arguments> validCurrentUser() {
        return Stream.of(
                Arguments.of(UserAuth.builder().username("kms").email("test@gmail.com").build()
        ));
    }

}