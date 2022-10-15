package com.io.realworld.domain.aggregate.profile.controller;

import com.io.realworld.domain.aggregate.profile.dto.Profile;
import com.io.realworld.domain.aggregate.profile.service.ProfilesService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
public class ProfilesController {
    private final ProfilesService profilesService;

    public ProfilesController(ProfilesService profilesService) {
        this.profilesService = profilesService;
    }

    @GetMapping("/{username}")
    public Profile getProfile(@RequestParam String username, @AuthenticationPrincipal UserAuth userAuth){
        return profilesService.getProfile(username, userAuth);
    }

}
