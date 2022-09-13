package com.io.realworld.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworld.DTO.UserSignupRequest;
import com.io.realworld.repository.User;
import com.io.realworld.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입_서비스_테스트")
    void signup() {
        // given
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserSignupRequest request = getUserSignupRequest();
        String encryptedPw = encoder.encode(request.getPassword());

        doReturn(new User(request.getUsername(), request.getEmail(), encryptedPw, "", "")).when(userRepository)
                .save(any(User.class));

        // when
        User user = userService.signup(request);

        // then
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(encoder.matches(request.getPassword(), user.getPassword())).isTrue();

        // verify
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }


    private UserSignupRequest getUserSignupRequest() {
        return UserSignupRequest.builder()
                .username("kms")
                .email("kms@gmail.com")
                .password("password")
                .build();
    }

    private User getUserResponse() {
        return User.builder()
                .username("kms")
                .email("kms@gamil.com")
                .bio("1")
                .image("image")
                .build();
    }
}