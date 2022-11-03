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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ArticleRepository articleRepository;
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentResponse addComment(UserAuth userAuth, String slug, Commentdto commentdto) {
        Optional<User> user = userRepository.findById(userAuth.getId());
        Optional<Article> article = articleRepository.findAll().stream().filter(findArticle -> findArticle.getSlug().equals(slug)).findAny();
        if (article.isEmpty()) {
            throw new CustomException(Error.ARTICLE_NOT_FOUND);
        }
        Comment comment = commentRepository.save(Comment.builder().body(commentdto.getBody()).article(article.get()).author(user.get()).build());

        return convertComment(userAuth, article.get(), comment);
    }

    private CommentResponse convertComment(UserAuth userAuth, Article article, Comment comment) {

        ProfileResponse profile = profileService.getProfile(userAuth, article.getAuthor().getUsername());

        return CommentResponse.builder()
                .id(comment.getId())
                .createAt(comment.getCreatedDate())
                .updateAt(comment.getModifiedDate())
                .body(comment.getBody())
                .author(CommentResponse.Author.builder()
                        .username(profile.getUsername())
                        .bio(profile.getBio())
                        .image(profile.getImage())
                        .following(profile.getFollowing()).build()).build();
    }
}
