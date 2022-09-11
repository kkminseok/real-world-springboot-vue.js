package com.io.realworld.api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworld.DTO.UserSignupRequest;
import com.io.realworld.repository.User;
import com.io.realworld.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserServiceImpl userService;


    @Test
    @WithMockUser
    @DisplayName("회원가입 컨트롤러 테스트")
    void signup() throws Exception {
        //given
        UserSignupRequest signupRequestTest = getUserSignupRequest();
        User signupResponseTest = getUserResponse();
        Mockito.doReturn(signupResponseTest).when(userService)
                .signup(any(UserSignupRequest.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestTest))
                        .with(csrf())
        );


        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", signupResponseTest.getEmail()).exists())
                .andExpect(jsonPath("$.user.username", signupResponseTest.getUsername()).exists())
                .andExpect(jsonPath("$.user.bio", signupResponseTest.getBio()).exists())
                .andExpect(jsonPath("$.user.image", signupResponseTest.getImage()).exists());


    }

    private UserSignupRequest getUserSignupRequest() {
        return UserSignupRequest.builder()
                .username("kms")
                .email("kms@gmail.com")
                .password("password")
                .build();
    }

    private User getUserResponse() {
        return User.builder()
                .username("kms")
                .email("kms@gamil.com")
                .bio("1")
                .image("image")
                .build();
    }
}