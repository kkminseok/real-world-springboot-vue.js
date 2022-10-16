package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.article.dto.ResponseArticle;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Override
    public ResponseArticle createArticle(UserAuth userAuth, Articledto article) {
        return null;
    }

}
