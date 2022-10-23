package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.ArticleResponse;
import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.article.repository.ArticleRepository;
import com.io.realworld.domain.aggregate.article.repository.FavoriteRepository;
import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.tag.service.TagService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @InjectMocks
    ArticleServiceImpl articleService;

    @Mock
    UserRepository userRepository;

    @Mock
    ArticleRepository articleRepository;

    @Mock
    TagService tagService;

    @Mock
    FavoriteRepository favoriteRepository;

    @Mock
    ProfileService profileService;

    @Test
    @DisplayName("sv: 게시글 만들기 성공")
    void createArticle() {
        UserAuth userAuth = UserAuth.builder().id(1L).username("kms").build();
        User user = User.builder().id(userAuth.getId()).build();
        Articledto article = Articledto.builder()
                .title("title create")
                .body("body create")
                .description("description create").
                tagList(Arrays.asList("tag1", "tag2")).
                build();

        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.ofNullable(user));
        when(profileService.getProfile(eq(userAuth), eq(userAuth.getUsername()))).thenReturn(ProfileResponse.builder().username(userAuth.getUsername()).build());

        ArticleResponse articleResponse = articleService.createArticle(userAuth, article);

        assertThat(articleResponse.getBody()).isEqualTo(article.getBody());
        assertThat(articleResponse.getDescription()).isEqualTo(article.getDescription());
        assertThat(articleResponse.getTitle()).isEqualTo(article.getTitle());
        assertThat(articleResponse.getTagList()).isEqualTo(article.getTagList());
        assertThat(articleResponse.getAuthor().getUsername().equals(userAuth.getUsername()));

    }
}