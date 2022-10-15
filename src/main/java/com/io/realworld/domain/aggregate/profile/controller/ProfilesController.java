package com.io.realworld.domain.aggregate.profile.controller;

<<<<<<< HEAD
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

=======
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


>>>>>>> 390c4b1e37d593dbb09dc676b3601311509fe90e
}
