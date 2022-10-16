package com.io.realworld.domain.aggregate.article.controller;

import com.io.realworld.domain.aggregate.article.dto.Article;
import com.io.realworld.domain.aggregate.article.dto.ResponseArticle;
import com.io.realworld.domain.aggregate.article.service.ArticleService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")

public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }


    @PostMapping
    public ResponseArticle createArticle(@AuthenticationPrincipal UserAuth userAuth, @RequestBody Article article){
        return articleService.createArticle(userAuth,article);
    }
}
