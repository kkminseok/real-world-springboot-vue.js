package com.io.realworld.domain.aggregate.user.service;

import com.io.realworld.domain.aggregate.user.dto.UserSigninRequest;
import com.io.realworld.domain.aggregate.user.dto.UserSignupRequest;
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
