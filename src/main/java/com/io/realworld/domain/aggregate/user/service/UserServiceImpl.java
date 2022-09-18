package com.io.realworld.domain.aggregate.user.service;

import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.dto.UserResponse;
import com.io.realworld.domain.aggregate.user.dto.UserSigninRequest;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    @Transactional
    public UserResponse signup(UserSignupRequest userSignupRequest) {
        if (userRepository.findByEmail(userSignupRequest.getEmail()) != null) {
            throw new CustomException(Error.DUPLICATE_USER);
        } else {
            return convertUser(userRepository.save(User.of(userSignupRequest.getUsername(),
                    userSignupRequest.getEmail(),
                    madeHash(userSignupRequest.getPassword()))));
        }
    }

    private String madeHash(String password) {
        return passwordEncoder.encode(password);
    }


    public UserResponse signin(UserSigninRequest userSigninRequest) {

        User findUser = userRepository.findByEmail(userSigninRequest.getEmail());
        if (findUser == null)
            throw new CustomException(Error.EMAIL_NULL_OR_INVALID);
        return convertUser(findUser);
    }

    @Override
    public UserResponse getCurrentUser(UserAuth userAuth) {
        User findUser = userRepository.findByEmail(userAuth.getEmail());
        if (findUser == null) {
            throw new CustomException(Error.EMAIL_NULL_OR_INVALID);
        } else {
            return convertUser(findUser);
        }
    }


    private UserResponse convertUser(User user){
        return UserResponse.builder().username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .token(jwtService.createToken(user.getEmail()))
                .build();
    }
}
