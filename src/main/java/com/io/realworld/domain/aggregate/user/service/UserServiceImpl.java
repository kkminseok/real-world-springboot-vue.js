package com.io.realworld.domain.aggregate.user.service;

import com.io.realworld.domain.aggregate.user.dto.*;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UserResponse signup(UserSignupRequest userSignupRequest) {
        User findEmail = userRepository.findByEmail(userSignupRequest.getEmail());
        Optional<User> findUsername = userRepository.findByUsername(userSignupRequest.getUsername());

        if ( findEmail != null &&  !findUsername.isEmpty()) {
            throw new CustomException(Error.DUPLICATE_EMAIL_USERNAME);
        }else if(findEmail != null){
            throw new CustomException(Error.DUPLICATE_EMAIL);
        }else if(!findUsername.isEmpty()){
            throw new CustomException(Error.DUPLICATE_USERNAME);
        }
        else {
            return convertUser(userRepository.save(User.builder().
                            username(userSignupRequest.getUsername()).
                            email(userSignupRequest.getEmail()).
                            password(madeHash(userSignupRequest.getPassword())).
                            image("https://api.realworld.io/images/smiley-cyrus.jpeg").build()));
        }
    }

    private String madeHash(String password) {
        return passwordEncoder.encode(password);
    }


    public UserResponse signin(UserSigninRequest userSigninRequest) {

        User findUser = userRepository.findByEmail(userSigninRequest.getEmail());
        if (findUser == null) {
            throw new CustomException(Error.EMAIL_NULL_OR_INVALID);
        }else{
            if(!passwordEncoder.matches(userSigninRequest.getPassword(), findUser.getPassword())){
                throw new CustomException(Error.PASSWORD_WRONG);
            }
        }

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

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdate userUpdate, UserAuth userAuth){
        User user = userRepository.findById(userAuth.getId()).orElseThrow(() -> {throw new CustomException(Error.USER_NOT_FOUND);});
        if(userUpdate.getEmail() != null){
            userRepository.findAllByEmail(userUpdate.getEmail())
                    .stream().filter(found -> !found.getId().equals(user.getId()))
                        .findFirst().ifPresent(found ->{throw new CustomException(Error.DUPLICATE_EMAIL);} );
            user.changeEmail(userUpdate.getEmail());
        }

        if(userUpdate.getUsername() != null){
            userRepository.findAllByUsername(userUpdate.getUsername())
                    .stream().filter(found -> !found.getId().equals(user.getId()))
                    .findFirst().ifPresent(found -> {throw new CustomException(Error.DUPLICATE_USERNAME);});
            user.changeUsername(userUpdate.getUsername());
        }
        if(userUpdate.getPassword() != null){
            user.changePassword(madeHash(userUpdate.getPassword()));
        }
        userUpdate.setId(user.getId());
        user.update(userUpdate);
        return convertUser(userRepository.save(user));
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
