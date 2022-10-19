package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.article.dto.ArticleResponse;
import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.entity.Favorite;
import com.io.realworld.domain.aggregate.article.repository.ArticleRepository;
import com.io.realworld.domain.aggregate.article.repository.FavoriteRepository;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.tag.entity.Tag;
import com.io.realworld.domain.aggregate.tag.service.TagService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
    public ArticleResponse createArticle(UserAuth userAuth, Articledto article) {
        Optional<User> findUser = userRepository.findById(userAuth.getId());
        String slug = initSlug(article.getTitle());


        Article articleEntity = Article.builder().slug(slug).body(article.getBody()).title(article.getTitle()).description(article.getDescription()).author(findUser.get()).build();
        List<Tag> tags = convertTag(article.getTagList(), articleEntity);
        articleEntity.setTagList(tags);
        log.debug(articleEntity.getTagList().get(0).getTagName());
        articleRepository.save(articleEntity);
        tagService.save(articleEntity);
        return convertDto(articleEntity, userAuth);
    }

    private String initSlug(String title) {
        return title.toLowerCase().replace(' ', '-');
    }

    private List<Tag> convertTag(List<String> tagNames,Article article){
        List<Tag> tags = new ArrayList<>();
        for(int i = 0;i< tagNames.size(); ++i){
            Tag tag = Tag.builder().tagName(tagNames.get(i)).article(article).build();
            tags.add(tag);
        }
        return tags;
    }

    private ArticleResponse convertDto(Article article, UserAuth userAuth) {
        // TODO : favoritesCount
        return ArticleResponse.builder().
                slug(article.getSlug()).
                title(article.getTitle()).
                description(article.getDescription()).
                body(article.getBody()).
                tagList(tagService.getTag(article.getId())).
                favorited(getFavoritesStatus(userAuth, article)).
                favoritesCount(0L).
                author(profileService.getProfile(userAuth, userAuth.getUsername())).build();

    }

    private Boolean getFavoritesStatus(UserAuth userAuth, Article article) {
        Optional<Favorite> favoriteStatus = favoriteRepository.findByArticleIdAndAuthorId(article.getId(), userAuth.getId());
        return false ? favoriteStatus.isEmpty() : true;
    }

}
