package com.io.realworld.domain.aggregate.article.repository;

import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.entity.Comment;
import com.io.realworld.domain.aggregate.profile.repository.ProfileRepository;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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
import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
class CommentRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

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
    @DisplayName("댓글 추가 테스트")
    void addComment(){
        Comment comment = Comment.builder().author(user).body("body").article(article).build();

        Comment savedComment = commentRepository.save(comment);

        assertThat(savedComment.getId()).isEqualTo(1L);
        assertThat(savedComment.getBody()).isEqualTo(comment.getBody());
        assertThat(savedComment.getAuthor().getId()).isEqualTo(user.getId());
        assertThat(savedComment.getAuthor().getEmail()).isEqualTo(user.getEmail());
        assertThat(savedComment.getAuthor().getUsername()).isEqualTo(user.getUsername());
        assertThat(savedComment.getAuthor().getBio()).isEqualTo(user.getBio());
        assertThat(savedComment.getAuthor().getImage()).isEqualTo(user.getImage());
        assertThat(savedComment.getCreatedDate()).isAfter(beforeCreated);
        assertThat(savedComment.getArticle().getBody()).isEqualTo(article.getBody());
        assertThat(savedComment.getArticle().getSlug()).isEqualTo(article.getSlug());
        assertThat(savedComment.getArticle().getDescription()).isEqualTo(article.getDescription());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteComment(){
        Comment comment = Comment.builder().author(user).body("body").article(article).build();

        commentRepository.save(comment);

        commentRepository.delete(comment);
        Optional<Comment> savedComment = commentRepository.findById(comment.getId());
        assertThat(savedComment).isEmpty();
    }

    private String initSlug(String title){
        return title.toLowerCase().replace(' ','-');
    }

}