package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.*;
import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.entity.Favorite;
import com.io.realworld.domain.aggregate.article.repository.ArticleRepository;
import com.io.realworld.domain.aggregate.article.repository.FavoriteRepository;
import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.entity.Follow;
import com.io.realworld.domain.aggregate.profile.repository.ProfileRepository;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.tag.entity.Tag;
import com.io.realworld.domain.aggregate.tag.service.TagService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Mock
    ProfileRepository profileRepository;


    @Test
    @DisplayName("sv: 게시글들 가져오기 - tag가 일치")
    void getArticlesTag(){
        UserAuth userAuth = UserAuth.builder().id(1L).username("kms").build();
        List<Article> articles = new ArrayList<Article>(){{
            add(articlesListGet().get(0));
        }};
        ArticleParam articleParam = new ArticleParam();
        articleParam.setTag("blogTag");

        when(profileService.getProfile(eq(userAuth), any(String.class))).thenReturn(ProfileResponse.builder().username(articles.get(0).getAuthor().getUsername()).build());
        when(articleRepository.findByTag(eq(articleParam.getTag()),any(Pageable.class))).thenReturn(articles);
        List<ArticleResponse> articleResponses = articleService.getArticles(userAuth, articleParam);

        assertThat(articleResponses.get(0).getTagList().get(0)).isEqualTo(articleParam.getTag());
    }

    @Test
    @DisplayName("sv: 게시글들 가져오기 - author가 일치")
    void getArticlesAuthor(){
        UserAuth userAuth = UserAuth.builder().id(1L).username("kms").build();

        ArticleParam articleParam = new ArticleParam();
        articleParam.setAuthor("jyb");

        List<Article> articles = articlesListGet().stream().filter(article -> {
            return article.getAuthor().getUsername().equals(articleParam.getAuthor());
        }).collect(Collectors.toList());


        when(profileService.getProfile(eq(userAuth), any(String.class))).thenReturn(ProfileResponse.builder().username(articles.get(0).getAuthor().getUsername()).build());
        when(articleRepository.findByAuthorName(eq(articleParam.getAuthor()),any(Pageable.class))).thenReturn(articles);
        List<ArticleResponse> articleResponses = articleService.getArticles(userAuth, articleParam);

        assertThat(articleResponses.get(0).getAuthor().getUsername()).isEqualTo(articleParam.getAuthor());
    }

    @Test
    @DisplayName("sv: 피드 게시글 가져오기 ")
    void getFeed(){
        UserAuth userAuth = UserAuth.builder().id(1L).username("kms").build();

        FeedParam feedParam = new FeedParam();

        Article article = articlesListGet().get(2);
        List<Article> articles = new ArrayList<>(){{
            add(article);
        }};

        List<Follow> follows = new ArrayList<Follow>(){{
            add(Follow.builder().followee(User.builder().username("kms").build()).follower(User.builder().username("eden").build()).build());
        }};

        when(profileService.getProfile(eq(userAuth), any(String.class))).thenReturn(ProfileResponse.builder().username(articles.get(0).getAuthor().getUsername()).build());
        when(profileRepository.findByFolloweeId(any(Long.class))).thenReturn(follows);
        when(articleRepository.findByAuthorName(any(String.class),any(Pageable.class))).thenReturn(articles);
        List<ArticleResponse> articleResponses = articleService.getFeed(userAuth, feedParam);

        assertThat(articleResponses.get(0).getAuthor().getUsername()).isEqualTo(follows.get(0).getFollower().getUsername());
    }


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
    void favoriteArticle(){
        User user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        List<Article> articles = List.of(Article.builder().id(1L).slug(slug).author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();
        ProfileResponse profileResponse = ProfileResponse.builder().bio("bio").following(false).username("username").image("image").build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.of(user));
        when(profileService.getProfile(eq(userAuth),any(String.class))).thenReturn(profileResponse);
        //첫번째는 없어야하고 두 번째때는 save이후이므로 있어야함.
        when(favoriteRepository.findByArticleIdAndUserId(any(Long.class), eq(user.getId())))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.ofNullable(Favorite.builder().build()));
        when(favoriteRepository.countByArticleId(any(Long.class)))
                .thenReturn(1L);

        ArticleResponse articleResponse = articleService.favoriteArticle(userAuth,slug);
        assertTrue(articleResponse.getFavorited());
        assertThat(articleResponse.getFavoritesCount()).isEqualTo(1L);

    }

    
    @Test
    @DisplayName("sv: 좋아요 실패 - 게시글 없음")
    void favoriteArticleFailArticleEmpty(){
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

    @Test
    @DisplayName("sv: 좋아요 실패 - 유저 못 찾음")
    void favoriteArticleFailUserNotFound(){
        User user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        List<Article> articles = List.of(Article.builder().id(1L).slug(slug).author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.empty());

        try{
            articleService.favoriteArticle(userAuth,slug);
        }catch (CustomException e){
            assertThat(e.getError()).isEqualTo(Error.USER_NOT_FOUND);
            assertThat(e.getError().getMessage()).isEqualTo(Error.USER_NOT_FOUND.getMessage());
            assertThat(e.getError().getStatus()).isEqualTo(Error.ARTICLE_NOT_FOUND.getStatus());
        }
    }

    @Test
    @DisplayName("sv: 좋아요 실패 - 이미 좋아요 누름")
    void favoriteArticleAlreadyFavorite(){
        User user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        List<Article> articles = List.of(Article.builder().id(1L).slug(slug).author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.of(user));
        when(favoriteRepository.findByArticleIdAndUserId(any(Long.class),any(Long.class))).thenReturn(Optional.ofNullable(Favorite.builder().build()));

        try{
            articleService.favoriteArticle(userAuth,slug);
        }catch (CustomException e){
            assertThat(e.getError()).isEqualTo(Error.ALREADY_FAVORITE_ARTICLE);
            assertThat(e.getError().getMessage()).isEqualTo(Error.ALREADY_FAVORITE_ARTICLE.getMessage());
            assertThat(e.getError().getStatus()).isEqualTo(Error.ALREADY_FAVORITE_ARTICLE.getStatus());
        }

    }

    @Test
    @DisplayName("sv: 안좋아요 성공")
    void unFavoriteArticle(){
        User user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        List<Article> articles = List.of(Article.builder().id(1L).slug(slug).author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();
        ProfileResponse profileResponse = ProfileResponse.builder().bio("bio").following(false).username("username").image("image").build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.of(user));
        when(profileService.getProfile(eq(userAuth),any(String.class))).thenReturn(profileResponse);

        //처음에는 있어야하고, 두 번째는 삭제이후이므로 객체가 비어있어야함.
        when(favoriteRepository.findByArticleIdAndUserId(any(Long.class), eq(user.getId())))
                .thenReturn(Optional.ofNullable(Favorite.builder().build()))
                .thenReturn(Optional.empty());
        when(favoriteRepository.countByArticleId(any(Long.class)))
                .thenReturn(0L);

        ArticleResponse articleResponse = articleService.unFavoriteArticle(userAuth,slug);
        assertFalse(articleResponse.getFavorited());
        assertThat(articleResponse.getFavoritesCount()).isEqualTo(0L);

    }

    @Test
    @DisplayName("sv: 안좋아요 실패 - 게시글 없음")
    void unFavoriteArticleFailArticleEmpty(){
        User user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        List<Article> articles = List.of(Article.builder().id(1L).slug("1").author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.of(user));

        try{
            articleService.unFavoriteArticle(userAuth,slug);
        }catch (CustomException e){
            assertThat(e.getError()).isEqualTo(Error.ARTICLE_NOT_FOUND);
            assertThat(e.getError().getMessage()).isEqualTo(Error.ARTICLE_NOT_FOUND.getMessage());
            assertThat(e.getError().getStatus()).isEqualTo(Error.ARTICLE_NOT_FOUND.getStatus());
        }
    }

    @Test
    @DisplayName("sv: 안좋아요 실패 - 유저 못 찾음")
    void unFavoriteArticleFailUserNotFound(){
        User user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        List<Article> articles = List.of(Article.builder().id(1L).slug(slug).author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.empty());

        try{
            articleService.unFavoriteArticle(userAuth,slug);
        }catch (CustomException e){
            assertThat(e.getError()).isEqualTo(Error.USER_NOT_FOUND);
            assertThat(e.getError().getMessage()).isEqualTo(Error.USER_NOT_FOUND.getMessage());
            assertThat(e.getError().getStatus()).isEqualTo(Error.ARTICLE_NOT_FOUND.getStatus());
        }
    }

    @Test
    @DisplayName("sv: 안좋아요 실패 - 이미 안좋아요 누름")
    void unFavoriteArticleAlreadyFavorite(){
        User user = User.builder().id(1L).username("kms").build();
        String slug = "slug";
        List<Article> articles = List.of(Article.builder().id(1L).slug(slug).author(user).tagList(List.of()).build());
        UserAuth userAuth = UserAuth.builder().id(1L).username(user.getUsername()).build();
        ProfileResponse profileResponse = ProfileResponse.builder().bio("bio").following(false).username("username").image("image").build();

        when(articleRepository.findAll()).thenReturn(articles);
        when(userRepository.findById(eq(userAuth.getId()))).thenReturn(Optional.of(user));
        when(favoriteRepository.findByArticleIdAndUserId(any(Long.class),any(Long.class))).thenReturn(Optional.ofNullable(Favorite.builder().build()));
        when(profileService.getProfile(eq(userAuth),any(String.class))).thenReturn(profileResponse);

        try{
            articleService.unFavoriteArticle(userAuth,slug);
        }catch (CustomException e){
            assertThat(e.getError()).isEqualTo(Error.ALREADY_UN_FAVORITE_ARTICLE);
            assertThat(e.getError().getMessage()).isEqualTo(Error.ALREADY_UN_FAVORITE_ARTICLE.getMessage());
            assertThat(e.getError().getStatus()).isEqualTo(Error.ALREADY_UN_FAVORITE_ARTICLE.getStatus());
        }

    }

    List<Article> articlesListGet(){
        List<Tag> blogTags = new ArrayList<Tag>(){{
         add(Tag.builder().tagName("blogTag").build());
         add(Tag.builder().tagName("tutorial").build());
        }};
        List<Tag> dietTags = new ArrayList<Tag>(){{
            add(Tag.builder().tagName("dietTag").build());
            add(Tag.builder().tagName("tutorial").build());
        }};
        Article blogPost = Article.builder()
                .id(1L)
                .slug("post-my-blog")
                .author(User.builder()
                        .username("kms")
                        .image("blog image")
                        .bio("blog bio")
                        .email("kms@naver.com").build())
                .body("blog Post very ez")
                .tagList(blogTags)
                .description("blog create")
                .title("Post My Blog")
                .build();

        Article dietPost = Article.builder()
                .id(2L)
                .slug("post-week-diet")
                .author(User.builder()
                        .username("jyb")
                        .image("diet image")
                        .bio("diet bio")
                        .email("jyb@naver.com").build())
                .body("diet very hard")
                .tagList(dietTags)
                .description("blog create")
                .title("Post week diet")
                .build();

        Article codePost = Article.builder()
                .id(3L)
                .slug("post-coding-test")
                .author(User.builder()
                        .username("eden")
                        .image("programmer")
                        .bio("s")
                        .email("tesla@google.com").build())
                .body("Tesla very nice")
                .tagList(dietTags)
                .description("realworldApp")
                .title("post Coding Test")
                .build();

        List<Article> articles = new ArrayList<Article>(){{
            add(blogPost);
            add(dietPost);
            add(codePost);
        }};
        return articles;
    }





}