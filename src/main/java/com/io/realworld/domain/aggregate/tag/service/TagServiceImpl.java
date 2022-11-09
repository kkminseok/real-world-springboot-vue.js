package com.io.realworld.domain.aggregate.tag.service;


import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.tag.entity.Tag;
import com.io.realworld.domain.aggregate.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;


    @Override
    public List<String> getTags() {
        return tagRepository.findAll().stream().map(Tag::getTagName).distinct().collect(Collectors.toList());
    }

}


