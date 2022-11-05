package com.io.realworld.domain.aggregate.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Getter
@JsonTypeName("article")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class ArticleResponse {
    private String slug;
    private String title;
    private String description;
    private String body;

    private List<String> tagList;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    private Boolean favorited;
    private Long favoritesCount;

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
