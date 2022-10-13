package com.io.realworld.domain.aggregate.profile.service;

import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.profile.repository.*;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{
    private final UserRepository userRepository;


    @Override
    public String getProfile(UserAuth userAuth, String username) {


        return null;
    }
}
