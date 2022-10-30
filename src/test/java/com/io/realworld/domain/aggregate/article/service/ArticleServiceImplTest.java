package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.ArticleResponse;
import com.io.realworld.domain.aggregate.article.dto.ArticleUpdate;
import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.entity.Favorite;
import com.io.realworld.domain.aggregate.article.repository.ArticleRepository;
import com.io.realworld.domain.aggregate.article.repository.FavoriteRepository;
import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.tag.service.TagService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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
        User user = User.builder().id(userAuth.getId()).username("username").build();
        Articledto article = Articledto.builder()
                .title("title create")
                .body("body create")
                .description("description create").
                tagList(Arrays.asList("tag1", "tag2")).
                build();

        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.ofNullable(user));
        when(profileService.getProfile(eq(userAuth), any(String.class))).thenReturn(ProfileResponse.builder().username(userAuth.getUsername()).build());

        ArticleResponse articleResponse = articleService.createArticle(userAuth, article);

        assertThat(articleResponse.getBody()).isEqualTo(article.getBody());
        assertThat(articleResponse.getDescription()).isEqualTo(article.getDescription());
        assertThat(articleResponse.getTitle()).isEqualTo(article.getTitle());
        assertThat(articleResponse.getTagList()).isEqualTo(article.getTagList());
        assertThat(articleResponse.getAuthor().getUsername().equals(userAuth.getUsername()));

    }

    @Test
    @DisplayName("sv: 게시글 찾기 실패")
    void getArticleFailNotFound(){
        List<Article> articles = new ArrayList<>();
        UserAuth userAuth = UserAuth.builder().id(1L).username("kms").build();
        String slug = "slug";

        when(articleRepository.findAll()).thenReturn(articles);
        try {
            articleService.getArticle(userAuth,slug);
        }catch (CustomException e){
            assertThat(e.getError()).isEqualTo(Error.ARTICLE_NOT_FOUND);
            assertThat(e.getError().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    @DisplayName("sv: 게시글 찾기 성공")
    void getArticleSuccess(){
        User user = User.builder().id(1L).username("username").build();
        ProfileResponse profileResponse = ProfileResponse.builder().bio("bio").following(false).username("username").image("image").build();
        List<Article> articles = List.of(Article.builder().slug("slug").author(user).tagList(List.of()).build());
        Article firstArticle = articles.get(0);
        UserAuth userAuth = UserAuth.builder().id(1L).username("kms").build();
        String slug = "slug";
        when(articleRepository.findAll()).thenReturn(articles);
        when(profileService.getProfile(eq(userAuth),any(String.class))).thenReturn(profileResponse);

        ArticleResponse article = articleService.getArticle(userAuth, slug);

        assertThat(article.getSlug()).isEqualTo(firstArticle.getSlug());
        assertThat(article.getAuthor().getBio()).isEqualTo(profileResponse.getBio());
        assertThat(article.getAuthor().getFollowing()).isEqualTo(profileResponse.getFollowing());
        assertThat(article.getAuthor().getUsername()).isEqualTo(profileResponse.getUsername());
        assertThat(article.getAuthor().getImage()).isEqualTo(profileResponse.getImage());

    }

    @Test
    @DisplayName("sv: 게시글 업데이트 실패 - 게시글이 없음")
    void updateArticleFailNotFound(){
        UserAuth userAuth = UserAuth.builder()
                        .build();
        String slug = "fail";
        ArticleUpdate articleUpdate = ArticleUpdate.builder().build();
        when(articleRepository.findAll()).thenReturn(List.of());
        try{
            articleService.updateArticle(userAuth,slug,articleUpdate);
        }catch (CustomException e){
            assertThat(e.getError().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(e.getError()).isEqualTo(Error.ARTICLE_NOT_FOUND);
            assertThat(e.getError().getMessage()).isEqualTo(Error.ARTICLE_NOT_FOUND.getMessage());
        }
    }

    @Test
    @DisplayName("게시글 업데이트 성공 - 타이틀 변경 ")
    void updateArticleTitle(){
        UserAuth userAuth = UserAuth.builder()
                .username("kms")
                .build();
        String title = "update title";
        String slug = "slug";
        User user = User.builder().id(1L).username("kms").build();
        ProfileResponse profileResponse = ProfileResponse.builder().bio("bio").following(false).username("username").image("image").build();
        List<Article> articles = List.of(Article.builder().title("title").slug(slug).author(user).tagList(List.of()).build());
        ArticleUpdate articleUpdate = ArticleUpdate.builder().title(title).build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(profileService.getProfile(eq(userAuth),any(String.class))).thenReturn(profileResponse);

        ArticleResponse articleResponse = articleService.updateArticle(userAuth,slug,articleUpdate);

        String updateSlug = title.toLowerCase().replace(' ','-');
        assertThat(articleResponse.getTitle()).isEqualTo(title);
        assertThat(articleResponse.getSlug()).isEqualTo(updateSlug);

    }

    @Test
    @DisplayName("게시글 업데이트 성공 - 본문 변경")
    void updateArticleBody(){
        UserAuth userAuth = UserAuth.builder()
                .username("kms")
                .build();
        String updateBody = "update body";
        String slug = "slug";
        User user = User.builder().id(1L).username("kms").build();
        ProfileResponse profileResponse = ProfileResponse.builder().bio("bio").following(false).username("username").image("image").build();
        List<Article> articles = List.of(Article.builder().slug(slug).body("body").author(user).tagList(List.of()).build());
        ArticleUpdate articleUpdate = ArticleUpdate.builder().body(updateBody).build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(profileService.getProfile(eq(userAuth),any(String.class))).thenReturn(profileResponse);

        ArticleResponse articleResponse = articleService.updateArticle(userAuth,slug,articleUpdate);

        assertThat(articleResponse.getBody()).isEqualTo(updateBody);

    }

    @Test
    @DisplayName("게시글 업데이트 성공 - 설명 변경")
    void updateArticleDescription(){
        UserAuth userAuth = UserAuth.builder()
                .username("kms")
                .build();
        String updateDescription = "update description";
        String slug = "slug";
        User user = User.builder().id(1L).username("kms").build();
        ProfileResponse profileResponse = ProfileResponse.builder().bio("bio").following(false).username("username").image("image").build();
        List<Article> articles = List.of(Article.builder().slug(slug).description("oo").author(user).tagList(List.of()).build());
        ArticleUpdate articleUpdate = ArticleUpdate.builder().description(updateDescription).build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(profileService.getProfile(eq(userAuth),any(String.class))).thenReturn(profileResponse);

        ArticleResponse articleResponse = articleService.updateArticle(userAuth,slug,articleUpdate);

        assertThat(articleResponse.getDescription()).isEqualTo(updateDescription);

    }

    @Test
    @DisplayName("sv: 게시글 삭제")
    void deleteArticle(){
        String slug = "slug";
        User user = User.builder().id(1L).username("kms").build();
        List<Article> articles = List.of(Article.builder().slug(slug).author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();

        when(articleRepository.findAll()).thenReturn(articles);
        doNothing().when(articleRepository).delete(any(Article.class));

        articleService.deleteArticle(userAuth,slug);
    }

    @Test
    @DisplayName("sv: 좋아요 성공")
    void favoritedArticle(){
        User user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        List<Article> articles = List.of(Article.builder().id(1L).slug(slug).author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();
        ProfileResponse profileResponse = ProfileResponse.builder().bio("bio").following(false).username("username").image("image").build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.of(user));
        when(profileService.getProfile(eq(userAuth),any(String.class))).thenReturn(profileResponse);
        //첫번째는 없어야하고 두 번째때는 save이후이므로 있어야함.
        when(favoriteRepository.findByArticleIdAndAuthorId(any(Long.class), eq(user.getId())))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.ofNullable(Favorite.builder().build()));
        when(favoriteRepository.countByArticleId(any(Long.class)))
                .thenReturn(1L);

        ArticleResponse articleResponse = articleService.favoriteArticle(userAuth,slug);
        assertTrue(articleResponse.getFavorited());
        assertThat(articleResponse.getFavoritesCount()).isEqualTo(1L);

    }

    //좋아요 실패 예외처리들.
    @Test
    @DisplayName("sv: 좋아요 실패 - 게시글 없음")
    void favoritedArticleFailArticleEmpty(){
        User user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        List<Article> articles = List.of(Article.builder().id(1L).slug("1").author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.of(user));

        try{
            articleService.favoriteArticle(userAuth,slug);
        }catch (CustomException e){
            assertThat(e.getError()).isEqualTo(Error.ARTICLE_NOT_FOUND);
            assertThat(e.getError().getMessage()).isEqualTo(Error.ARTICLE_NOT_FOUND.getMessage());
            assertThat(e.getError().getStatus()).isEqualTo(Error.ARTICLE_NOT_FOUND.getStatus());
        }
    }



    //좋아요 멀티인 경우 - 좋아요가 3개일때

}