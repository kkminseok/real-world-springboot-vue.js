package com.io.realworld.domain.aggregate.article.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleParam {
    private String tag;
    private String author;
    private String favorited;
    private Integer limit;
    private Integer offset;

}
