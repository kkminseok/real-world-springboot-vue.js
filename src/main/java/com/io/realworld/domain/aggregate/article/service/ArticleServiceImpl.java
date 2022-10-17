package com.io.realworld.domain.aggregate.article.service;

import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.article.dto.ArticleResponse;
import com.io.realworld.domain.aggregate.article.entity.Article;
import com.io.realworld.domain.aggregate.article.entity.Favorite;
import com.io.realworld.domain.aggregate.article.repository.ArticleRepository;
import com.io.realworld.domain.aggregate.article.repository.FavoriteRepository;
import com.io.realworld.domain.aggregate.profile.service.ProfileService;
import com.io.realworld.domain.aggregate.tag.entity.Tag;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    private final FavoriteRepository favoriteRepository;

    @Override
    public ArticleResponse createArticle(UserAuth userAuth, Articledto article) {
        Optional<User> findUser = userRepository.findById(userAuth.getId());
        String slug = initSlug(article.getTitle());
        Article articleEntity = Article.builder().slug(slug).body(article.getBody()).title(article.getTitle()).description(article.getDescription()).author(findUser.get()).build();
        articleRepository.save(articleEntity);
        return convertDto(articleEntity,userAuth);
    }

    private String initSlug(String title){
        return title.toLowerCase().replace(' ','-');
    }

    private ArticleResponse convertDto(Article article,UserAuth userAuth){
        // TODO : favoritesCount
        return ArticleResponse.builder().
                slug(article.getSlug()).
                title(article.getTitle()).
                description(article.getDescription()).
                body(article.getBody()).
                tagList(getTagList(article.getTagList())).
                favorited(getFavoritesStatus(userAuth,article)).
                favoritesCount(0L).
                author(profileService.getProfile(userAuth,userAuth.getUsername())).build();

    }
    private List<String> getTagList(List<Tag> tagList){
        List<String> result = null;
        for(int i =0; i < tagList.size(); ++i){
            Tag tag = tagList.get(i);
            result.add(tag.getTagName());
        }
        return result;
    }

    private Boolean getFavoritesStatus(UserAuth userAuth, Article article){
        Optional<Favorite> favoriteStatus = favoriteRepository.findByArticleIdAndUserId(article.getId(), userAuth.getId());
        return false ? favoriteStatus.isEmpty() : true;
    }

}
