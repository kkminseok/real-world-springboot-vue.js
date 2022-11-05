package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.CommentResponse;
import com.io.realworld.domain.aggregate.article.dto.Commentdto;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getComments(UserAuth userAuth, String slug);

    CommentResponse addComment(UserAuth userAuth, String slug, Commentdto commentdto);

    void deleteComment(UserAuth userAuth, String slug, Long id);
}
