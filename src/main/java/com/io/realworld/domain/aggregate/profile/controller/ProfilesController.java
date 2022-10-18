package com.io.realworld.domain.aggregate.profile.controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfilesController {

    private final ProfileService profileService;

    public ProfilesController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public ProfileResponse getProfile(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("username") String username) {
        return profileService.getProfile(userAuth, username);
    }

    @PostMapping("/{username}/follow")
    public ProfileResponse followUser(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("username") String username) {
        return profileService.followUser(userAuth, username);
    }

    @DeleteMapping("{username}/follow")
    public ProfileResponse unfollowUser(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("username") String username) {
        return profileService.unfollowUser(userAuth, username);
    }

}
