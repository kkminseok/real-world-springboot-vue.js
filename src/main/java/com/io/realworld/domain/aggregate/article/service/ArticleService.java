package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.Article;
import com.io.realworld.domain.aggregate.article.dto.ResponseArticle;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import org.springframework.stereotype.Service;

@Service
public interface ArticleService {
    ResponseArticle createArticle(UserAuth userAuth, Article article);

}
