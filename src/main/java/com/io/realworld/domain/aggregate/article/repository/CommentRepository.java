package com.io.realworld.domain.aggregate.article.repository;

import com.io.realworld.domain.aggregate.article.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
