package com.io.realworld.domain.aggregate.article.repository;

import com.io.realworld.domain.aggregate.article.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {

    @EntityGraph(attributePaths = "tagList")
    @Query("SELECT a FROM Article a JOIN Tag t ON a.id = t.article.id")
    List<Article> findByTag(String tag, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.author.username = :author")
    List<Article> findByAuthorName(String author, Pageable pageable);

    @Query("SELECT a FROM Article a LEFT JOIN Favorite f ON f.article.id = a.id WHERE f.user.username =:username")
    List<Article> findByFavoritedUser(String username, Pageable pageable);
}
