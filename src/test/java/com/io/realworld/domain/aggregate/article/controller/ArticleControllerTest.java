package com.io.realworld.domain.aggregate.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.realworld.config.WithAuthUser;
import com.io.realworld.domain.aggregate.article.dto.*;
import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.repository.ArticleRepository;
import com.io.realworld.domain.aggregate.article.service.ArticleService;
import com.io.realworld.domain.aggregate.article.service.CommentService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.service.JwtService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
    CommentService commentService;

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
                        .username("kms")
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

    @WithAuthUser
    @Test
    @DisplayName("게시글들 가져오기 컨트롤러 테스트")
    void getArticles() throws Exception{
        List<ArticleResponse> articleResponses = List.of(articleResponse);
        when(articleService.getArticles(any(UserAuth.class), any(ArticleParam.class))).thenReturn(articleResponses);

        mockMvc.perform(get("/api/articles" + "?author=kms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles[0]", Matchers.notNullValue(ArticleResponse.class)))
                .andExpect(jsonPath("$.articles[0].title",Matchers.equalTo(articleResponses.get(0).getTitle())))
                .andExpect(jsonPath("$.articles[0].description",Matchers.equalTo(articleResponses.get(0).getDescription())))
                .andExpect(jsonPath("$.articles[0].body",Matchers.equalTo(articleResponses.get(0).getBody())))
                .andExpect(jsonPath("$.articles[0].slug",Matchers.equalTo(articleResponses.get(0).getSlug())))
                .andExpect(jsonPath("$.articles[0].tagList",Matchers.equalTo(articleResponses.get(0).getTagList())));
    }

    @WithAuthUser
    @Test
    @DisplayName("팔로우한 유저 게시글 가져오기 컨트롤러 테스트")
    void getFeed() throws Exception{
        List<ArticleResponse> articleResponses = List.of(articleResponse);
        when(articleService.getFeed(any(UserAuth.class), any(FeedParam.class))).thenReturn(articleResponses);

        mockMvc.perform(get("/api/articles/feed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles[0]", Matchers.notNullValue(ArticleResponse.class)))
                .andExpect(jsonPath("$.articles[0].title",Matchers.equalTo(articleResponses.get(0).getTitle())))
                .andExpect(jsonPath("$.articles[0].description",Matchers.equalTo(articleResponses.get(0).getDescription())))
                .andExpect(jsonPath("$.articles[0].body",Matchers.equalTo(articleResponses.get(0).getBody())))
                .andExpect(jsonPath("$.articles[0].slug",Matchers.equalTo(articleResponses.get(0).getSlug())))
                .andExpect(jsonPath("$.articles[0].tagList",Matchers.equalTo(articleResponses.get(0).getTagList())));
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
    void favoriteArticle() throws Exception{
        when(articleService.favoriteArticle(any(UserAuth.class),eq(slug))).thenReturn(articleResponse);

        mockMvc.perform(post("/api/articles/" + slug + "/favorite")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.article.body", Matchers.equalTo(articleResponse.getBody())))
                .andExpect(jsonPath("$.article.title",Matchers.equalTo(articleResponse.getTitle())))
                .andExpect(jsonPath("$.article.description", Matchers.equalTo(articleResponse.getDescription())))
                .andExpect(jsonPath("$.article.slug", Matchers.equalTo(slug)));
    }

    @WithAuthUser
    @Test
    @DisplayName("게시글 안좋아요 컨트롤러 테스트")
    void unFavoriteArticle() throws Exception{
        when(articleService.unFavoriteArticle(any(UserAuth.class),eq(slug))).thenReturn(articleResponse);

        mockMvc.perform(delete("/api/articles/" + slug + "/favorite")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.article.body", Matchers.equalTo(articleResponse.getBody())))
                .andExpect(jsonPath("$.article.title",Matchers.equalTo(articleResponse.getTitle())))
                .andExpect(jsonPath("$.article.description", Matchers.equalTo(articleResponse.getDescription())))
                .andExpect(jsonPath("$.article.slug", Matchers.equalTo(slug)));
    }

    @WithAuthUser
    @Test
    @DisplayName("댓글 가져오기 컨트롤러 테스트")
    void getComment() throws Exception{
        CommentResponse commentResponse = CommentResponse.builder().body("body").build();
        List<CommentResponse> commentResponses = List.of(commentResponse);

        when(commentService.getComments(any(UserAuth.class), eq(slug))).thenReturn(commentResponses);

        mockMvc.perform(get("/api/articles/" + slug + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0]",Matchers.notNullValue(CommentResponse.class)));
    }

    @GetMapping("/{slug}/comments")
    public CommentResponse.MultiComments getComments(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug){
        return CommentResponse.MultiComments.builder().comments(commentService.getComments(userAuth, slug)).build();
    }

    @WithAuthUser
    @Test
    @DisplayName("댓글 달기 컨트롤러 테스트")
    void createComment() throws Exception{
        String body = "comment body";
        CommentResponse commentResponse = CommentResponse.builder().body(body).build();
        Commentdto commentdto = Commentdto.builder().body(body).build();

        when(commentService.addComment(any(UserAuth.class),eq(slug),any(Commentdto.class))).thenReturn(commentResponse);

        mockMvc.perform(post("/api/articles/" + slug + "/comments")
                        .content(objectMapper.writeValueAsString(commentdto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment.body",Matchers.equalTo(commentdto.getBody())));
    }

    @WithAuthUser
    @Test
    @DisplayName("댓글 삭제 컨트롤러 테스트")
    void deleteComment() throws Exception{
        Long commentId = 1L;
        doNothing().when(commentService).deleteComment(any(UserAuth.class), eq(slug), eq(commentId));

        mockMvc.perform(delete("/api/articles/" + slug + "/comments/" + commentId))
                .andExpect(status().isOk());
    }

}