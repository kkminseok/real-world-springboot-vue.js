package com.io.realworld.domain.aggregate.article.repository;

import com.io.realworld.domain.aggregate.article.dto.ArticleUpdate;
import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.profile.entity.Follow;
import com.io.realworld.domain.aggregate.profile.repository.ProfileRepository;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
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

    private User author;
    private Article article;
    private LocalDateTime beforeCreated;

    @BeforeEach
    void setup(){
        author = User.builder()
                .bio("bio")
                .email("email")
                .image("image")
                .password("password")
                .username("username").build();

        userRepository.save(author);

        String title = "create title";
        String slug = initSlug(title);
        beforeCreated = LocalDateTime.now();
        article = Article.builder()
                .author(author)
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

        Article article2 = Article.builder().author(author).title("title").body("body").description("description").slug("slug").build();
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

    private String initSlug(String title){
        return title.toLowerCase().replace(' ','-');
    }

    //Todo 좋아요 3개이상이상일때


}