package com.io.realworld.domain.aggregate.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@Getter
@JsonTypeName("comment")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class CommentResponse {
    private Long id;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
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
