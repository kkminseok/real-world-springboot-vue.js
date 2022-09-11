package com.io.realworld.service;

import com.io.realworld.DTO.UserSignupRequest;
import com.io.realworld.Exception.CustomException;
import com.io.realworld.Exception.Error;
import com.io.realworld.repository.User;
import com.io.realworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;



    @Transactional
    public User signup(UserSignupRequest userSignupRequest) {
        if (userRepository.findByEmail(userSignupRequest.getEmail()) != null) {
            throw new CustomException(Error.DUPLICATE_USER);
        } else {
            String hashPw = madeHash(userSignupRequest.getPassword());
            return userRepository.save(User.of(userSignupRequest.getUsername(),
                    userSignupRequest.getEmail(),
                    hashPw));
        }
    }

    private String madeHash(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return  passwordEncoder.encode(password);
    }

}
