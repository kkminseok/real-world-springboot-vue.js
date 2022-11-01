package com.io.realworld.domain.aggregate.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@JsonTypeName("comment")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class CommentResponse {
    private Long id;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String body;

    private Author author;

    @Builder
    @Getter
    public static class Author{
        private String username;
        private String bio;
        private String image;
        private Boolean following;
    }
}
