package com.io.realworld.domain.aggregate.tag.controller;

import com.io.realworld.domain.aggregate.tag.dto.TagResponse;
import com.io.realworld.domain.aggregate.tag.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    //Todo Response tags: {} ~
    @GetMapping
    public List<String> getTags(){
        return tagService.getTags();
    }
}
