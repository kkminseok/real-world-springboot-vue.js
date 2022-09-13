package com.io.realworld.service;

import com.io.realworld.DTO.UserSigninRequest;
import com.io.realworld.DTO.UserSignupRequest;
import com.io.realworld.Exception.CustomException;
import com.io.realworld.Exception.Error;
import com.io.realworld.repository.User;
import com.io.realworld.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User signup(UserSignupRequest userSignupRequest) {
        if (userRepository.findByEmail(userSignupRequest.getEmail()) != null) {
            throw new CustomException(Error.DUPLICATE_USER);
        } else {
            return userRepository.save(User.of(userSignupRequest.getUsername(),
                    userSignupRequest.getEmail(),
                    madeHash(userSignupRequest.getPassword())));
        }
    }

    private String madeHash(String password){
        return  passwordEncoder.encode(password);
    }

    @Transactional
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User signin(UserSigninRequest userSigninRequest) {

        User findUser = userRepository.findByEmail(userSigninRequest.getEmail());
        if(findUser == null)
            throw new CustomException(Error.SIGNIN_EMAILNULL_OR_INVALID);
        return findUser;
    }
}
