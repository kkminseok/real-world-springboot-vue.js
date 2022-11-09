package com.io.realworld.domain.aggregate.tag.service;

import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.tag.dto.TagResponse;
import com.io.realworld.domain.aggregate.tag.entity.Tag;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TagService {
    List<String> getTags();
}
