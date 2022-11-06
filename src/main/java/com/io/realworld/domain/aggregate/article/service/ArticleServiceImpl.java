package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.ArticleParam;
import com.io.realworld.domain.aggregate.article.dto.ArticleUpdate;
import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.article.dto.ArticleResponse;
import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.entity.Favorite;
import com.io.realworld.domain.aggregate.article.repository.ArticleRepository;
import com.io.realworld.domain.aggregate.article.repository.FavoriteRepository;
import com.io.realworld.domain.aggregate.profile.dto.ProfileResponse;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.tag.entity.Tag;
import com.io.realworld.domain.aggregate.tag.service.TagService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import com.io.realworld.exception.CustomException;
import com.io.realworld.exception.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final TagService tagService;
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    private final FavoriteRepository favoriteRepository;

    @Override
    public  List<ArticleResponse> getArticles(UserAuth userAuth, ArticleParam articleParam){
        Pageable pageable = null;
        List<Article> articles = null;

        if(articleParam.getOffset() != null){
            pageable = PageRequest.of(articleParam.getOffset(), articleParam.getLimit());
        }

        if(articleParam.getTag() != null){
            articles = articleRepository.findByTag(articleParam.getTag(),pageable);
        }


        return List.of();
    }

    // token을 받을수도 안 받을수도 있음.
    @Override
    public ArticleResponse getArticle(UserAuth userAuth, String slug) {
        Optional<Article> article = articleRepository.findAll().stream().filter(findArticle -> findArticle.getSlug().equals(slug)).findAny();
        if (article.isEmpty()) {
            throw new CustomException(Error.ARTICLE_NOT_FOUND);
        }
        return convertDtoWithUser(article.get(), userAuth);
    }

    //하나만 세이브되지 않도록 원자성 보장.
    @Transactional
    @Override
    public ArticleResponse createArticle(UserAuth userAuth, Articledto article) {
        Optional<User> findUser = userRepository.findById(userAuth.getId());
        String slug = initSlug(article.getTitle());
        Article articleEntity = Article.builder().slug(slug).body(article.getBody()).title(article.getTitle()).description(article.getDescription()).author(findUser.get()).build();
        List<Tag> tags = convertTag(article.getTagList(), articleEntity);
        articleEntity.setTagList(tags);

        articleRepository.save(articleEntity);
        tagService.save(articleEntity);

        return convertDtoWithUser(articleEntity, userAuth);
    }


    @Transactional
    @Override
    public ArticleResponse updateArticle(UserAuth userAuth, String slug, ArticleUpdate articleUpdate) {
        Optional<Article> article = articleRepository.findAll().stream().filter(findArticle ->
            findArticle.getSlug().equals(slug) && findArticle.getAuthor().getUsername().equals(userAuth.getUsername())
        ).findAny();
        if (article.isEmpty()) { throw new CustomException(Error.ARTICLE_NOT_FOUND); }

        if (articleUpdate.getTitle() != null) {
            article.get().changeTitle(articleUpdate.getTitle());
            article.get().changeSlug(initSlug(articleUpdate.getTitle()));
        }
        if(articleUpdate.getDescription() != null){ article.get().changeDescription(articleUpdate.getDescription()); }
        if(articleUpdate.getBody() != null){ article.get().changeBody(articleUpdate.getBody()); }

        return convertDtoWithUser(article.get(),userAuth);

    }

    public void deleteArticle(UserAuth userAuth, String slug) {
        Stream<Article> articles = articleRepository.findAll().stream().filter(findArticle -> findArticle.getSlug().equals(slug));
        for (Article article : articles.toList()) {
            if (article.getAuthor().getUsername().equals(userAuth.getUsername())) {
                articleRepository.delete(article);
            }
        }
    }

    public ArticleResponse favoriteArticle(UserAuth userAuth, String slug){
        Optional<Article> article = articleRepository.findAll().stream().filter(findArticle ->
                findArticle.getSlug().equals(slug)).findAny();
        Optional<User> user = userRepository.findById(userAuth.getId());
        if(article.isEmpty()){
            throw new CustomException(Error.ARTICLE_NOT_FOUND);
        }
        if(user.isEmpty()){
            throw new CustomException(Error.USER_NOT_FOUND);
        }

        favoriteRepository.findByArticleIdAndUserId(article.get().getId(), userAuth.getId()).ifPresent(favoriteStatus -> {throw new CustomException(Error.ALREADY_FAVORITE_ARTICLE);});
        Favorite favorite = Favorite.builder().article(article.get()).user(user.get()).build();
        favoriteRepository.save(favorite);
        return convertDtoWithUser(article.get(),userAuth);

    }

    @Override
    public ArticleResponse unFavoriteArticle(UserAuth userAuth, String slug) {
        Optional<Article> article = articleRepository.findAll().stream().filter(findArticle ->
                findArticle.getSlug().equals(slug)).findAny();
        Optional<User> user = userRepository.findById(userAuth.getId());
        if(article.isEmpty()){
            throw new CustomException(Error.ARTICLE_NOT_FOUND);
        }
        if(user.isEmpty()){
            throw new CustomException(Error.USER_NOT_FOUND);
        }

        Favorite favorite = favoriteRepository.findByArticleIdAndUserId(article.get().getId(), userAuth.getId()).orElseThrow(() -> {
            throw new CustomException(Error.ALREADY_UN_FAVORITE_ARTICLE);
        });
        favoriteRepository.delete(favorite);
        return convertDtoWithUser(article.get(),userAuth);
    }

    private String initSlug(String title) {
        return title.toLowerCase().replace(' ', '-');
    }

    private List<Tag> convertTag(List<String> tagNames, Article article) {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < tagNames.size(); ++i) {
            Tag tag = Tag.builder().tagName(tagNames.get(i)).article(article).build();
            tags.add(tag);
        }
        return tags;
    }

    private ArticleResponse convertDtoWithUser(Article article, UserAuth userAuth) {
        ProfileResponse profile = profileService.getProfile(userAuth, article.getAuthor().getUsername());

        return ArticleResponse.builder().
                slug(article.getSlug()).
                title(article.getTitle()).
                description(article.getDescription()).
                body(article.getBody()).
                tagList(article.getTagList().stream().map(Tag::getTagName).distinct().collect(Collectors.toList())).
                favorited(getFavoritesStatus(userAuth, article)).
                favoritesCount(getFavoritesCount(article.getId())).
                createdAt(article.getCreatedDate()).
                updatedAt(article.getModifiedDate()).
                author(ArticleResponse.Author.builder().username(profile.getUsername())
                        .image(profile.getImage())
                        .following(profile.getFollowing())
                        .bio(profile.getBio()).build()
                ).build();
    }

    private Boolean getFavoritesStatus(UserAuth userAuth, Article article) {
        Optional<Favorite> favoriteStatus = favoriteRepository.findByArticleIdAndUserId(article.getId(), userAuth.getId());
        return favoriteStatus.isEmpty() ? false : true;
    }

    private Long getFavoritesCount(Long articleId) {
        return favoriteRepository.countByArticleId(articleId);
    }

}
