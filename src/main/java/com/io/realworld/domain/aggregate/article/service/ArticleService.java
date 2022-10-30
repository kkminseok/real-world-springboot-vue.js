package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.ArticleParam;
import com.io.realworld.domain.aggregate.article.dto.ArticleUpdate;
import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.article.dto.ArticleResponse;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;

import java.util.List;


public interface ArticleService {
    List<ArticleResponse> getArticles(ArticleParam articleParam);

    ArticleResponse getArticle(UserAuth userAuth, String slug);

    ArticleResponse createArticle(UserAuth userAuth, Articledto article);

    ArticleResponse updateArticle(UserAuth userAuth, String slug, ArticleUpdate articleUpdate);

    void deleteArticle(UserAuth userAuth, String slug);

}
