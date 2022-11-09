package com.io.realworld.domain.aggregate.article.controller;

import com.io.realworld.domain.aggregate.article.dto.*;
import com.io.realworld.domain.aggregate.article.service.ArticleService;
import com.io.realworld.domain.aggregate.article.service.CommentService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping()
    public ArticleResponse.MultiArticles getArticles(@AuthenticationPrincipal UserAuth userAuth, @ModelAttribute ArticleParam articleParam) {
        List<ArticleResponse> articles = articleService.getArticles(userAuth, articleParam);
        return ArticleResponse.MultiArticles.builder().articles(articles).articlesCount(articles.size()).build();
    }

    @GetMapping("/{slug}")
    public ArticleResponse.SingleArticle getArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug) {
        return ArticleResponse.SingleArticle.builder().article(articleService.getArticle(userAuth, slug)).build();
    }

    @PostMapping
    public ArticleResponse.SingleArticle createArticle(@AuthenticationPrincipal UserAuth userAuth, @Valid @RequestBody Articledto articledto) {
        return ArticleResponse.SingleArticle.builder().article(articleService.createArticle(userAuth, articledto)).build();
    }

    @PutMapping("/{slug}")
    public ArticleResponse.SingleArticle updateArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug, @Valid @RequestBody ArticleUpdate articleUpdate) {
        return ArticleResponse.SingleArticle.builder().article(articleService.updateArticle(userAuth, slug, articleUpdate)).build();
    }

    @DeleteMapping("/{slug}")
    public void deleteArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug) {
        articleService.deleteArticle(userAuth, slug);
    }

    @PostMapping("/{slug}/favorite")
    public ArticleResponse.SingleArticle favoriteArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug) {
        return ArticleResponse.SingleArticle.builder().article(articleService.favoriteArticle(userAuth, slug)).build();
    }

    @DeleteMapping("/{slug}/favorite")
    public ArticleResponse.SingleArticle unFavoriteArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug) {
        return ArticleResponse.SingleArticle.builder().article(articleService.unFavoriteArticle(userAuth, slug)).build();
    }

    @GetMapping("/{slug}/comments")
    public CommentResponse.MultiComments getComments(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug){
        return CommentResponse.MultiComments.builder().comments(commentService.getComments(userAuth, slug)).build();
    }

    @PostMapping("/{slug}/comments")
    public CommentResponse.SingleComment createComment(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug, @Valid @RequestBody Commentdto commentdto) {
        return CommentResponse.SingleComment.builder().comment(commentService.addComment(userAuth, slug, commentdto)).build();
    }

    @DeleteMapping("/{slug}/comments/{id}")
    public void deleteComment(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug, @PathVariable("id") Long id) {
        commentService.deleteComment(userAuth, slug, id);
    }
}
