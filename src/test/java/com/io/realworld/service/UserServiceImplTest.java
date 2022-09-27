package com.io.realworld.service;

import com.io.realworld.domain.aggregate.user.dto.*;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import com.io.realworld.domain.aggregate.user.service.UserServiceImpl;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;


    @MethodSource("validUsers")
    @ParameterizedTest
    @DisplayName("회원가입_서비스_테스트")
    void signup(UserSignupRequest requestUser) {

        User user = User.builder()
                .username(requestUser.getUsername())
                .bio("")
                .email(requestUser.getEmail())
                .image("")
                .password("password")
                .build();

        doReturn(user).when(userRepository)
                .save(any(User.class));
        doReturn("token").when(jwtService).createToken(any(String.class));
        UserResponse savedUser = userService.signup(requestUser);


        // then
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getToken()).isNotNull();

        // verify
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }


    @MethodSource("duplicateUsers")
    @ParameterizedTest
    @DisplayName("회원가입_중복_서비스_테스트")
    void duplicateSignup(UserSignupRequest requestUser) {
        User user = User.builder()
                .username(requestUser.getUsername())
                .bio("")
                .email(requestUser.getEmail())
                .image("")
                .password(requestUser.getPassword())
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(null).thenThrow(new CustomException(Error.DUPLICATE_USER));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.signup(requestUser);
    }

    @MethodSource("validLoginUsers")
    @ParameterizedTest
    @DisplayName("로그인 성공 서비스 테스트")
    void loginSuccess(UserSigninRequest userSigninRequest){
        User user = User.builder()
                .bio("")
                .email(userSigninRequest.getEmail())
                .image("")
                .password(userSigninRequest.getPassword())
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);
        userService.signin(userSigninRequest);
    }

    @MethodSource("invalidLoginUsers")
    @ParameterizedTest
    @DisplayName("로그인 실패 서비스 테스트")
    void loginFail(UserSigninRequest userSigninRequest){

        when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        try {
            userService.signin(userSigninRequest);
        }catch (CustomException e){
            assertThat(e.getError().equals(Error.EMAIL_NULL_OR_INVALID));
            assertThat(e.getError().getMessage().equals(Error.EMAIL_NULL_OR_INVALID.getMessage()));
        }
    }

    @Test
    @DisplayName("현재 유저 가져오기 성공 테스트")
    void currentUserSuccess(){
        UserAuth userAuth = UserAuth.builder()
                .bio("bio")
                .email("authEmail@gmail.com")
                .id(1L)
                .username("authName")
                .image("image").build();

        User user = User.builder()
                .username(userAuth.getUsername())
                .email(userAuth.getEmail())
                .image("iamge")
                .username("authName")
                .password("test password").build();

        when(userRepository.findByEmail(eq(userAuth.getEmail()))).thenReturn(user);

        UserResponse getUser = userService.getCurrentUser(userAuth);
        assertThat(getUser.getEmail()).isEqualTo(userAuth.getEmail());
    }

    @DisplayName("현재유저 가져오기 실패 서비스 테스트")
    @Test
    void currentUserFail(){
        UserAuth userAuth = UserAuth.builder().id(1L).build();
        try {
            userService.getCurrentUser(userAuth);
        }catch (CustomException e){
            assertThat(e.getError().equals(Error.EMAIL_NULL_OR_INVALID));
            assertThat(e.getError().getMessage().equals(Error.EMAIL_NULL_OR_INVALID.getMessage()));
        }
    }

    @DisplayName("유저 업데이트 성공 테스트")
    @Test
    void updateUserSuccess(){
        UserUpdate userUpdate = UserUpdate.builder().username("fixuser").email("kms@gmail.com").build();
        UserAuth userAuth = UserAuth.builder().id(1L).build();
        User responseUser = User.builder().username(userUpdate.getUsername()).email(userUpdate.getEmail()).build();

        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.of(responseUser));
        when(userRepository.save(any(User.class))).thenReturn(responseUser);
        UserResponse userResponse = userService.updateUser(userUpdate, userAuth);

        assertEquals(userUpdate.getUsername(), userResponse.getUsername());
        assertEquals(userUpdate.getEmail(), userResponse.getEmail());
        assertNotNull(userResponse.getEmail());
        assertNotNull(userResponse.getUsername());

    }

    @DisplayName("유저 업데이트 실패 테스트")
    @MethodSource("invalidUpdateUsers")
    @ParameterizedTest
    void updateUserFailCauseFoundNot(UserUpdate userUpdate, UserAuth userAuth){
        UserAuth repoUser = UserAuth.builder().id(1L).username("update").email("update@gmail.com").build();

        when(userRepository.findById(AdditionalMatchers.not(eq(repoUser.getId())))).thenThrow(new CustomException(Error.USER_NOT_FOUND));
        // case 2
        if(userUpdate.getUsername() != null){
                lenient().when(userRepository.findByUsername(eq(repoUser.getUsername()))).thenThrow(new CustomException(Error.DUPLICATE_USER));
            try{
                userService.updateUser(userUpdate,userAuth);
            }catch(CustomException e){
                assertThat(e.getError().equals(Error.DUPLICATE_USER));
                assertThat(e.getError().getMessage().equals(Error.DUPLICATE_USER.getMessage()));
            }
        }
        // case 3
        else if(userUpdate.getEmail() != null){
            lenient().when(userRepository.findByEmail(eq(repoUser.getEmail()))).thenThrow(new CustomException(Error.DUPLICATE_USER));
            try{
                userService.updateUser(userUpdate,userAuth);
            }catch(CustomException e){
                assertThat(e.getError().equals(Error.DUPLICATE_USER));
                assertThat(e.getError().getMessage().equals(Error.DUPLICATE_USER.getMessage()));
            }
        }
        // case 1
        else {
            try {
                userService.updateUser(userUpdate, userAuth);
            } catch (CustomException e) {
                assertThat(e.getError().equals(Error.USER_NOT_FOUND));
                assertThat(e.getError().getMessage().equals(Error.USER_NOT_FOUND.getMessage()));
            }
        }
    }




    public static Stream<Arguments> validUsers() {
        return Stream.of(
                Arguments.of(UserSignupRequest.builder().username("kms").email("kms@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("jyb").email("yyb95@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("kms").email("realWorld@gmail.com").password("password").build())
        );
    }

    private static Stream<Arguments> duplicateUsers() {
        return Stream.of(
                Arguments.of(UserSignupRequest.builder().username("kms").email("kms@gmail.com").password("password").build()),
                Arguments.of(UserSignupRequest.builder().username("kms").email("kms@gmail.com").password("password").build())
        );
    }

    private static Stream<Arguments> validLoginUsers(){
        return Stream.of(
                Arguments.of(UserSigninRequest.builder().email("kms@naver.com").password("password").build()),
                Arguments.of(UserSigninRequest.builder().email("jjy@gmail.com").password("password").build())
        );
    }

    private static Stream<Arguments> invalidLoginUsers(){
        return Stream.of(
                Arguments.of(UserSigninRequest.builder().email("invalidEmail").password("password").build())
        );
    }
    private static Stream<Arguments> invalidUpdateUsers(){
        return Stream.of(
                Arguments.of(UserUpdate.builder().username("update").email("update@gmail.com").id(1L).password("password").build(),
                        UserAuth.builder().id(3L).build()),
                Arguments.of(UserUpdate.builder().username("update").email("update@gmail.com").id(1L).password("password").build(),
                        UserAuth.builder().id(3L).build()),
                Arguments.of(UserUpdate.builder().email("update@gmail.com").id(1L).password("password").build(),
                        UserAuth.builder().id(3L).build())
        );
    }

}