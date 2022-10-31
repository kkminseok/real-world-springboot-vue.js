package com.io.realworld.domain.aggregate.tag.service;


import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.tag.dto.TagResponse;
import com.io.realworld.domain.aggregate.tag.entity.Tag;
import com.io.realworld.domain.aggregate.tag.repository.TagRepository;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public void save(Article article) {
        List<Tag> tags = article.getTagList();
        for (int i = 0; i < tags.size(); ++i) {
            Tag tag = Tag.builder().article(article).tagName(tags.get(i).getTagName()).build();
            tagRepository.save(tag);
        }
    }

    @Override
    public List<String> getTags() {
        return tagRepository.findAll().stream().map(Tag::getTagName).distinct().collect(Collectors.toList());
    }
}
