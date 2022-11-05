package com.io.realworld.domain.aggregate.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Getter
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

    @Builder
    @Getter
    public static class SingleComment{
        CommentResponse comment;
    }

    @Builder
    @Getter
    public static class MultiComments{
        List<CommentResponse> comments;
    }
}
