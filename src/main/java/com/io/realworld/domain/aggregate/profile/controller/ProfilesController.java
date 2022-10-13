package com.io.realworld.domain.aggregate.profile.controller;

import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.profile.service.ProfileServiceImpl;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
public class ProfilesController {

    private final ProfileService profileService;

    public ProfilesController(ProfileService profileService){
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public String getProfile(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String username){
        return profileService.getProfile(userAuth,username);
    }


}
