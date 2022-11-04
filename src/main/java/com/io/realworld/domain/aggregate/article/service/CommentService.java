package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.CommentResponse;
import com.io.realworld.domain.aggregate.article.dto.Commentdto;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;

public interface CommentService {

    CommentResponse addComment(UserAuth userAuth, String slug, Commentdto commentdto);

    void deleteComment(UserAuth userAuth, String slug, Long id);
}
