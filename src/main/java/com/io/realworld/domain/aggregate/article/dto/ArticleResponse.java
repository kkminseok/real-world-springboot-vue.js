package com.io.realworld.domain.aggregate.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean favorited;
    private Long favoritesCount;

    private ProfileResponse author;



}
