package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.CommentResponse;
import com.io.realworld.domain.aggregate.article.dto.Commentdto;
import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.entity.Comment;
import com.io.realworld.domain.aggregate.article.repository.ArticleRepository;
import com.io.realworld.domain.aggregate.article.repository.CommentRepository;
import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.service.ProfileServiceImpl;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    CommentServiceImpl commentService;
    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;
    @Mock
    ArticleRepository articleRepository;
    @Mock
    ProfileServiceImpl profileService;

    private User user;
    private List<Article> articles;
    private UserAuth userAuth;
    private ProfileResponse profileResponse;

    @BeforeEach
    void setup(){
        user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        articles = List.of(Article.builder().id(1L).slug(slug).author(user).tagList(List.of()).build());
        userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();
        profileResponse = ProfileResponse.builder().bio("bio").following(false).username("username").image("image").build();


    }

    @Test
    @DisplayName("댓글 추가 테스트")
    void addComment() {
        String slug = "slug";
        Commentdto commentdto = Commentdto.builder().body("body").build();
        Comment comment = Comment.builder().id(1L).body("body").build();
        UserAuth userAuth = UserAuth.builder().username("kms").id(1L).build();


        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.of(user));
        when(profileService.getProfile(eq(userAuth),any(String.class))).thenReturn(profileResponse);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);


        CommentResponse commentResponse = commentService.addComment(userAuth, slug, commentdto);

        assertThat(commentResponse.getBody()).isEqualTo(commentdto.getBody());
        assertThat(commentResponse.getId()).isEqualTo(1L);
        assertThat(commentResponse.getAuthor().getUsername()).isEqualTo(profileResponse.getUsername());
        assertThat(commentResponse.getAuthor().getImage()).isEqualTo(profileResponse.getImage());

    }
}