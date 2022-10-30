package com.io.realworld.domain.aggregate.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworld.config.WithAuthUser;
import com.io.realworld.domain.aggregate.article.dto.ArticleResponse;
import com.io.realworld.domain.aggregate.article.dto.ArticleUpdate;
import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.article.service.ArticleService;
import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.dto.UserUpdate;
import com.io.realworld.domain.service.JwtService;
import lombok.With;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ArticleController.class)
class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ArticleService articleService;

    @MockBean
    JwtService jwtService;

    private ArticleResponse articleResponse;
    private Articledto articledto;

    private String slug;

    @BeforeEach
    void setup(){

        String title = "create title";
        slug = title.toLowerCase().replace(' ','-');
        articleResponse = ArticleResponse.builder()
                .author(ArticleResponse.Author.builder().bio("bio")
                        .following(false)
                        .username("madeArticle")
                        .image("image")
                        .build())
                .body("create body")
                .description("create description")
                .favorited(false)
                .favoritesCount(0L)
                .slug(slug)
                .title("create title")
                .build();

        articledto = Articledto.builder()
                .body("create body")
                .description("create description")
                .title("create title")
                .build();
    }

    @WithAuthUser(email = "test@gmail.com", username = "kms", id = 1L)
    @Test
    @DisplayName("게시글 만들기 컨트롤러 테스트")
    void createArticle() throws Exception {
        when(articleService.createArticle(any(UserAuth.class), any(Articledto.class))).thenReturn(articleResponse);

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articledto))
                        .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.article.body", Matchers.equalTo(articledto.getBody())))
                .andExpect(jsonPath("$.article.title", Matchers.equalTo(articledto.getTitle())))
                .andExpect(jsonPath("$.article.description", Matchers.equalTo(articledto.getDescription())));
    }

    @WithAuthUser(email = "test@gmail.com", username = "kms", id = 1L)
    @Test
    @DisplayName("게시글 조회 컨트롤러 테스트")
    void getArticle() throws Exception{

        when(articleService.getArticle(any(UserAuth.class),eq(slug))).thenReturn(articleResponse);

        mockMvc.perform(get("/api/articles/" + slug)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.article.body", Matchers.equalTo(articleResponse.getBody())))
                .andExpect(jsonPath("$.article.title",Matchers.equalTo(articleResponse.getTitle())))
                .andExpect(jsonPath("$.article.description", Matchers.equalTo(articleResponse.getDescription())))
                .andExpect(jsonPath("$.article.slug", Matchers.equalTo(slug)));
    }

    @WithAuthUser
    @Test
    @DisplayName("게시글 업데이트 title만 컨트롤러 테스트")
    void updateTitleArticle() throws Exception{
        ArticleUpdate articleUpdate = ArticleUpdate.builder()
                .title("update title")
                .build();

        ArticleResponse titleUpdateArticle = ArticleResponse.builder()
                        .title(articleUpdate.getTitle())
                        .build();


        when(articleService.updateArticle(any(UserAuth.class), eq(slug), any(ArticleUpdate.class))).thenReturn(titleUpdateArticle);

        mockMvc.perform(put("/api/articles/" + slug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleUpdate))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.title", Matchers.equalTo(articleUpdate.getTitle())));
    }

    @WithAuthUser
    @Test
    @DisplayName("게시글 업데이트 body만 컨트롤러 테스트")
    void updateBodyArticle() throws Exception{
        ArticleUpdate articleUpdate = ArticleUpdate.builder()
                .body("update body")
                .build();

        ArticleResponse titleUpdateArticle = ArticleResponse.builder()
                .body(articleUpdate.getBody())
                .build();


        when(articleService.updateArticle(any(UserAuth.class), eq(slug), any(ArticleUpdate.class))).thenReturn(titleUpdateArticle);

        mockMvc.perform(put("/api/articles/" + slug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleUpdate))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.body", Matchers.equalTo(articleUpdate.getBody())));
    }

    @WithAuthUser
    @Test
    @DisplayName("게시글 업데이트 description 컨트롤러 테스트")
    void updateDescriptionArticle() throws Exception{
        ArticleUpdate articleUpdate = ArticleUpdate.builder()
                .description("update description")
                .build();

        ArticleResponse titleUpdateArticle = ArticleResponse.builder()
                .description(articleUpdate.getDescription())
                .build();


        when(articleService.updateArticle(any(UserAuth.class), eq(slug), any(ArticleUpdate.class))).thenReturn(titleUpdateArticle);

        mockMvc.perform(put("/api/articles/" + slug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleUpdate))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.description", Matchers.equalTo(articleUpdate.getDescription())));
    }

    @WithAuthUser
    @Test
    @DisplayName("게시글 업데이트 title, body만 컨트롤러 테스트")
    void updateTitleAndBodyArticle() throws Exception{
        ArticleUpdate articleUpdate = ArticleUpdate.builder()
                .title("update title")
                .body("update body")
                .build();

        ArticleResponse titleUpdateArticle = ArticleResponse.builder()
                .title(articleUpdate.getTitle())
                .body(articleUpdate.getBody())
                .build();


        when(articleService.updateArticle(any(UserAuth.class), eq(slug), any(ArticleUpdate.class))).thenReturn(titleUpdateArticle);

        mockMvc.perform(put("/api/articles/" + slug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleUpdate))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.title", Matchers.equalTo(articleUpdate.getTitle())))
                .andExpect(jsonPath("$.article.body", Matchers.equalTo(articleUpdate.getBody())));
    }

    @WithAuthUser
    @Test
    @DisplayName("게시글 업데이트 전부 컨트롤러 테스트")
    void updateAllArticle() throws Exception{
        ArticleUpdate articleUpdate = ArticleUpdate.builder()
                .title("update title")
                .body("update body")
                .description("update description")
                .build();

        ArticleResponse titleUpdateArticle = ArticleResponse.builder()
                .title(articleUpdate.getTitle())
                .body(articleUpdate.getBody())
                .description(articleUpdate.getDescription())
                .build();


        when(articleService.updateArticle(any(UserAuth.class), eq(slug), any(ArticleUpdate.class))).thenReturn(titleUpdateArticle);

        mockMvc.perform(put("/api/articles/" + slug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleUpdate))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.title", Matchers.equalTo(articleUpdate.getTitle())))
                .andExpect(jsonPath("$.article.body", Matchers.equalTo(articleUpdate.getBody())))
                .andExpect(jsonPath("$.article.description", Matchers.equalTo(articleUpdate.getDescription())));
    }



    @WithAuthUser(email = "user@email.com", username = "kms")
    @Test
    @DisplayName("게시글 삭제 컨트롤러 테스트")
    void deleteArticle() throws Exception{

        doNothing().when(articleService).deleteArticle(any(UserAuth.class),eq(slug));

        mockMvc.perform(delete("/api/articles/" + slug)
                ).andExpect(status().isOk());
    }

    @WithAuthUser
    @Test
    @DisplayName("게시글 좋아요 컨트롤러 테스트")
    void favoritedArticle() throws Exception{
        when(articleService.favoriteArticle(any(UserAuth.class),eq(slug))).thenReturn(articleResponse);

        mockMvc.perform(post("/api/articles/" + slug + "/favorite")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.article.body", Matchers.equalTo(articleResponse.getBody())))
                .andExpect(jsonPath("$.article.title",Matchers.equalTo(articleResponse.getTitle())))
                .andExpect(jsonPath("$.article.description", Matchers.equalTo(articleResponse.getDescription())))
                .andExpect(jsonPath("$.article.slug", Matchers.equalTo(slug)));
    }

}