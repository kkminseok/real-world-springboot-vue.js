package com.io.realworld.domain.aggregate.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworld.config.WithAuthUser;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.dto.UserUpdate;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import com.io.realworld.domain.service.JwtService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @WithAuthUser(email = "test@gmail.com",username = "kms",id = 1L)
    @MethodSource("updateUser")
    @ParameterizedTest(name = "controller:유저 업데이트 성공 테스트")
    void updateUserSuccess(UserUpdate userUpdate) throws Exception{
        UserResponse userResponse = UserResponse.builder()
                .username("update name")
                .email("update@gmail.com")
                .build();
        when(userService.updateUser(any(UserUpdate.class),any(UserAuth.class))).thenReturn(userResponse);
        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate))
                        .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", Matchers.equalTo(userUpdate.getEmail())));
    }

    private static Stream<Arguments> updateUser(){
        return Stream.of(
                Arguments.of(UserUpdate.builder().email("update@gmail.com").build()
                ));
    }

}