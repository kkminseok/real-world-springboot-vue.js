package com.io.realworld.domain.aggregate.article.repository;

import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.profile.entity.Follow;
import com.io.realworld.domain.aggregate.profile.repository.ProfileRepository;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        beforeCreated = LocalDateTime.now();
        article = Article.builder()
                .author(author)
                .body("create body")
                .description("create description")
                .title(title)
                .slug(title.toLowerCase().replace(' ', '-'))
                .favorites(List.of())
                .tagList(List.of()).build();

        articleRepository.save(article);
    }

    @Test
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
    void getArticle_not_following(){

    }

    @Test
    void getArticle_following(){
        User reader = User.builder()
                .bio("bio")
                .email("email@google.com")
                .image("image")
                .password("password")
                .username("followee").build();

        userRepository.save(reader);
        Follow follow = Follow.builder().followee(reader).follower(author).build();
        profileRepository.save(follow);

        Optional<Article> savedArticle = articleRepository.findById(article.getId());
        System.out.println(savedArticle.get().getAuthor());

    }


}