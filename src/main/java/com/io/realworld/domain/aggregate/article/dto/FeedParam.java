package com.io.realworld.domain.aggregate.article.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedParam {
    private Integer offset;
    private Integer limit;
}
