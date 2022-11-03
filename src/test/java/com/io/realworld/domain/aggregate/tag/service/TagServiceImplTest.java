package com.io.realworld.domain.aggregate.tag.service;

import com.io.realworld.domain.aggregate.tag.entity.Tag;
import com.io.realworld.domain.aggregate.tag.repository.TagRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    TagServiceImpl tagService;

    @Mock
    TagRepository tagRepository;


    @Test
    @DisplayName("태그 리스트 가져오기")
    void getTags() {
        List<Tag> tagList = new ArrayList<>();
        Tag tag1 = Tag.builder().tagName("tag1").build();
        Tag tag2 = Tag.builder().tagName("tag2").build();
        tagList.add(tag1);
        tagList.add(tag2);

        when(tagRepository.findAll()).thenReturn(tagList);

        List<String> tags = tagService.getTags();

        assertThat(tags.size()).isEqualTo(2);
        assertThat(tags.get(0)).isEqualTo("tag1");
        assertThat(tags.get(1)).isEqualTo("tag2");

    }


}