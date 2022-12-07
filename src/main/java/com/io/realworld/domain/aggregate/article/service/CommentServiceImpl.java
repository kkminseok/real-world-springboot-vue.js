package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.CommentResponse;
import com.io.realworld.domain.aggregate.article.dto.Commentdto;
import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.entity.Comment;
import com.io.realworld.domain.aggregate.article.repository.ArticleRepository;
import com.io.realworld.domain.aggregate.article.repository.CommentRepository;
import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ArticleRepository articleRepository;
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentResponse> getComments(UserAuth userAuth, String slug){
        Optional<Article> article = articleRepository.findAll().stream().filter(findArticle -> findArticle.getSlug().equals(slug)).findAny();
        if (article.isEmpty()) {
            throw new CustomException(Error.ARTICLE_NOT_FOUND);
        }
        List<Comment> comments = commentRepository.findAll().stream().filter(findComment -> findComment.getArticle().getId().equals(article.get().getId())).collect(Collectors.toList());
        return comments.stream().map(comment-> {
            ProfileResponse profile = profileService.getProfile(userAuth, comment.getAuthor().getUsername());
            return CommentResponse.builder()
                    .id(comment.getId())
                    .createdAt(comment.getCreatedDate())
                    .updatedAt(comment.getModifiedDate())
                    .body(comment.getBody())
                    .author(CommentResponse.Author.builder()
                            .bio(profile.getBio())
                            .image(profile.getImage())
                            .following(profile.getFollowing())
                            .username(profile.getUsername())
                            .build()).build();
        }).collect(Collectors.toList());

    }

    @Override
    public CommentResponse addComment(UserAuth userAuth, String slug, Commentdto commentdto) {
        Optional<User> user = userRepository.findById(userAuth.getId());
        Optional<Article> article = articleRepository.findAll().stream().filter(findArticle -> findArticle.getSlug().equals(slug)).findAny();
        if (article.isEmpty()) {
            throw new CustomException(Error.ARTICLE_NOT_FOUND);
        }
        System.out.println(user.get().getUsername()+"!!");
        Comment comment = commentRepository.save(Comment.builder().body(commentdto.getBody()).article(article.get()).author(user.get()).build());

        return convertComment(userAuth, comment);
    }

    @Override
    public void deleteComment(UserAuth userAuth, String slug, Long id) {
        Optional<User> user = userRepository.findById(userAuth.getId());
        Optional<Article> article = articleRepository.findAll().stream().filter(findArticle -> findArticle.getSlug().equals(slug)).findAny();
        if (article.isEmpty()) {
            throw new CustomException(Error.ARTICLE_NOT_FOUND);
        }
        Optional<Comment> comment = commentRepository.findAll().stream().filter(findComment -> findComment.getId().equals(id)).findAny();
        if (comment.isEmpty()) {
            throw new CustomException(Error.Comment_NOT_FOUND);
        }
        commentRepository.delete(comment.get());
    }

    private CommentResponse convertComment(UserAuth userAuth, Comment comment) {

        ProfileResponse profile = profileService.getProfile(userAuth, userAuth.getUsername());

        return CommentResponse.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedDate())
                .updatedAt(comment.getModifiedDate())
                .body(comment.getBody())
                .author(CommentResponse.Author.builder()
                        .username(profile.getUsername())
                        .bio(profile.getBio())
                        .image(profile.getImage())
                        .following(profile.getFollowing()).build()).build();
    }
}
