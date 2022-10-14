package com.io.realworld.domain.aggregate.profile.controller;

import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/profiles")
public class ProfilesController {

    private final ProfileService profileService;

    public ProfilesController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public ProfileResponse getProfile(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("username") String username) {
        log.info("current user :{}, find user : {}", userAuth.getUsername(), username);
        return profileService.getProfile(userAuth, username);
    }

    @PostMapping("/{username}/follow")
    public ProfileResponse followUser(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("username") String username) {
        log.info("current user : {} , Follow User : {}",userAuth.getUsername(), username);
        return profileService.followUser(userAuth, username);
    }


}
