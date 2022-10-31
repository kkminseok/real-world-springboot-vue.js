package com.io.realworld.domain.aggregate.tag.controller;

import com.io.realworld.domain.aggregate.article.controller.ArticleController;
import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.tag.dto.TagResponse;
import com.io.realworld.domain.aggregate.tag.service.TagServiceImpl;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.service.JwtService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = TagController.class)
class TagControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TagServiceImpl tagService;

    @MockBean
    JwtService jwtService;

    @Test
    @DisplayName("태그 리스트 가져오기")
    void getTags() throws Exception {

        when(tagService.getTags()).thenReturn(List.of("tag1","tag2"));

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags[0]",Matchers.equalTo("tag1")))
                .andExpect(jsonPath("$.tags[1]",Matchers.equalTo("tag2")));
    }

}