package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.*;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;

import java.util.List;


public interface ArticleService {
    List<ArticleResponse> getArticles(UserAuth userAuth, ArticleParam articleParam);

    List<ArticleResponse> getFeed(UserAuth userAuth, FeedParam feedParam);

    ArticleResponse getArticle(UserAuth userAuth, String slug);

    ArticleResponse createArticle(UserAuth userAuth, Articledto article);

    ArticleResponse updateArticle(UserAuth userAuth, String slug, ArticleUpdate articleUpdate);

    void deleteArticle(UserAuth userAuth, String slug);

    ArticleResponse favoriteArticle(UserAuth userAuth, String slug);

    ArticleResponse unFavoriteArticle(UserAuth userAuth, String slug);



}
