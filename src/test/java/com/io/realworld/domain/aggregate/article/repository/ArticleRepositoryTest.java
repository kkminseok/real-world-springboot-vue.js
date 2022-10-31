package com.io.realworld.domain.aggregate.article.repository;

import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.entity.Favorite;
import com.io.realworld.domain.aggregate.profile.repository.ProfileRepository;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@EnableJpaAuditing
class ArticleRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    private User user;
    private Article article;
    private LocalDateTime beforeCreated;

    @BeforeEach
    void setup(){
        user = User.builder()
                .bio("bio")
                .email("email")
                .image("image")
                .password("password")
                .username("username").build();

        userRepository.save(user);

        String title = "create title";
        String slug = initSlug(title);
        beforeCreated = LocalDateTime.now();
        article = Article.builder()
                .author(user)
                .body("create body")
                .description("create description")
                .title(title)
                .slug(slug)
                .favorites(List.of())
                .tagList(List.of()).build();

        articleRepository.save(article);
    }

    @Test
    @DisplayName("rp: 게시글 만들기")
    void createArticle() {
        Optional<Article> savedArticle = articleRepository.findById(article.getId());

        Article article2 = Article.builder().author(user).title("title").body("body").description("description").slug("slug").build();
        articleRepository.save(article2);

        assertThat(article.getBody()).isEqualTo(savedArticle.get().getBody());
        assertThat(article.getDescription()).isEqualTo(savedArticle.get().getDescription());
        assertThat(article.getId()).isEqualTo(savedArticle.get().getId());
        assertThat(article.getTitle()).isEqualTo(savedArticle.get().getTitle());
        assertThat(article.getSlug()).isEqualTo(savedArticle.get().getSlug());
        assertThat(article.getTagList()).isEmpty();
        assertThat(article.getCreatedDate()).isBefore(article2.getCreatedDate());
        assertThat(article.getFavorites()).isEmpty();
        assertThat(article.getModifiedDate()).isBefore(article2.getModifiedDate());

    }

    @Test
    @DisplayName("rp: 게시글 조회")
    void getArticle(){
        Optional<Article> savedArticle = articleRepository.findById(article.getId());
        assertTrue(savedArticle.isPresent());
    }

    @Test
    @DisplayName("rp: 게시글 업데이트")
    void updateArticle(){
        String title = "update title";
        String body = "update body";
        String description = "update description";
        String slug = initSlug(title);
        article.changeTitle(title);
        article.changeBody(body);
        article.changeDescription(description);
        article.changeSlug(slug);
        Optional<Article> savedArticle = articleRepository.findById(article.getId());
        assertThat(savedArticle.get().getTitle()).isEqualTo(title);
        assertThat(savedArticle.get().getBody()).isEqualTo(body);
        assertThat(savedArticle.get().getDescription()).isEqualTo(description);
        assertThat(savedArticle.get().getSlug()).isEqualTo(slug);

    }

    @Test
    @DisplayName("rp: 게시글 삭제")
    void deleteArticle(){
        articleRepository.delete(article);
        List<Article> savedArticle = articleRepository.findAll();
        assertTrue(savedArticle.isEmpty());
    }

    @Test
    @DisplayName("rp: 좋아요 성공")
    void favoriteArticle(){

        Favorite favorite = Favorite.builder().article(article).user(user).build();
        favoriteRepository.save(favorite);

        Optional<Favorite> favoriteStatus = favoriteRepository.findByArticleIdAndUserId(article.getId(), user.getId());

        assertTrue(!favoriteStatus.isEmpty());
        assertThat(favoriteRepository.countByArticleId(article.getId())).isEqualTo(1L);

    }

    @Test
    @DisplayName("rp: 좋아요 3개이상")
    void favoriteArticleMulti(){
        User user2 = User.builder().email("email2@gmail.com").username("username2").password("pa").build();
        User user3 = User.builder().email("email3@gmail.com").username("username3").password("pa").build();
        userRepository.save(user2);
        userRepository.save(user3);

        Favorite favorite = Favorite.builder().article(article).user(user).build();
        Favorite favorite2 = Favorite.builder().article(article).user(user2).build();
        Favorite favorite3 = Favorite.builder().article(article).user(user3).build();

        assertTrue(favoriteRepository.findByArticleIdAndUserId(article.getId(), user.getId()).isEmpty());
        assertTrue(favoriteRepository.findByArticleIdAndUserId(article.getId(), user2.getId()).isEmpty());
        assertTrue(favoriteRepository.findByArticleIdAndUserId(article.getId(), user3.getId()).isEmpty());

        favoriteRepository.save(favorite);
        favoriteRepository.save(favorite2);
        favoriteRepository.save(favorite3);


        assertTrue(!favoriteRepository.findByArticleIdAndUserId(article.getId(), user.getId()).isEmpty());
        assertTrue(!favoriteRepository.findByArticleIdAndUserId(article.getId(), user2.getId()).isEmpty());
        assertTrue(!favoriteRepository.findByArticleIdAndUserId(article.getId(), user3.getId()).isEmpty());

        assertThat(favoriteRepository.countByArticleId(article.getId())).isEqualTo(3L);

    }

    @Test
    @DisplayName("rp: 안좋아요 성공")
    void unFavoriteArticle(){

        Favorite favorite = Favorite.builder().article(article).user(user).build();
        favoriteRepository.save(favorite);

        Optional<Favorite> favoriteStatus = favoriteRepository.findByArticleIdAndUserId(article.getId(), user.getId());

        assertTrue(!favoriteStatus.isEmpty());
        assertThat(favoriteRepository.countByArticleId(article.getId())).isEqualTo(1L);

        favoriteRepository.delete(favoriteStatus.get());

        assertThat(favoriteRepository.countByArticleId(article.getId())).isEqualTo(0L);

    }

    private String initSlug(String title){
        return title.toLowerCase().replace(' ','-');
    }


}