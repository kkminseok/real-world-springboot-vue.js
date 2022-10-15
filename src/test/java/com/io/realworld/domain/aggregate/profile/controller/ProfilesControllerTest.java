package com.io.realworld.domain.aggregate.profile.controller;


import com.io.realworld.config.WithAuthUser;
import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.service.JwtService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ProfilesController.class)
class ProfilesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProfileService profileService;

    @MockBean
    JwtService jwtService;


    @Test
    @WithAuthUser(email = "test@gmail.com",username = "kms",id = 1L)
    @DisplayName("유저 프로필 찾기 컨트롤러 테스트")
    void getProfile() throws Exception {
        String username = "profileUser";
        ProfileResponse profileResponse = ProfileResponse.builder().
                username(username).
                bio("bio").
                image("image").
                following(false).build();
        when(profileService.getProfile(any(UserAuth.class), eq(username))).thenReturn(profileResponse);
        mockMvc.perform(get("/api/profiles/"+ username)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username", Matchers.equalTo(profileResponse.getUsername())))
                .andExpect(jsonPath("$.profile.bio", Matchers.equalTo(profileResponse.getBio())))
                .andExpect(jsonPath("$.profile.image", Matchers.equalTo(profileResponse.getImage())))
                .andExpect(jsonPath("$.profile.following", Matchers.equalTo(profileResponse.getFollowing())));
    }

    @Test
    @WithAuthUser(email = "test@gmail.com",username = "kms",id = 1L)
    @DisplayName("유저 팔로우하기 컨트롤러 테스트")
    void followUser() throws Exception {
        String username = "profileUser";
        ProfileResponse profileResponse = ProfileResponse.builder().
                username(username).
                bio("bio").
                image("image").
                following(true).build();
        when(profileService.followUser(any(UserAuth.class), eq(username))).thenReturn(profileResponse);
        mockMvc.perform(post("/api/profiles/"+ username + "/follow")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username", Matchers.equalTo(profileResponse.getUsername())))
                .andExpect(jsonPath("$.profile.bio", Matchers.equalTo(profileResponse.getBio())))
                .andExpect(jsonPath("$.profile.image", Matchers.equalTo(profileResponse.getImage())))
                .andExpect(jsonPath("$.profile.following", Matchers.equalTo(profileResponse.getFollowing())));
    }

    @Test
    @WithAuthUser(email = "test@gmail.com",username = "kms",id = 1L)
    @DisplayName("유저 언팔로우하기 컨트롤러 테스트")
    void unfollowUser() throws Exception{
        String username = "profileUser";
        ProfileResponse profileResponse = ProfileResponse.builder().
                username(username).
                bio("bio").
                image("image").
                following(false).build();
        when(profileService.unfollowUser(any(UserAuth.class),eq(username))).thenReturn(profileResponse);
        mockMvc.perform(delete("/api/profiles/" + username + "/follow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username", Matchers.equalTo(profileResponse.getUsername())))
                .andExpect(jsonPath("$.profile.bio", Matchers.equalTo(profileResponse.getBio())))
                .andExpect(jsonPath("$.profile.image", Matchers.equalTo(profileResponse.getImage())))
                .andExpect(jsonPath("$.profile.following", Matchers.equalTo(profileResponse.getFollowing())));
    }
}