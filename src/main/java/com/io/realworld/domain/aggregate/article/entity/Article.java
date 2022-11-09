package com.io.realworld.domain.aggregate.article.entity;

import com.io.realworld.base.entity.DateEntity;
import com.io.realworld.domain.aggregate.tag.entity.Tag;
import com.io.realworld.domain.aggregate.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articles")
public class Article extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String body;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tag> tagList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User author;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Favorite> favorites;


    public void setTagList(List<Tag> tags){
        if(this.getTagList() == null){
            this.tagList = new ArrayList<>();
        }
        Collections.sort(tags, new Comparator<Tag>() {
            @Override
            public int compare(Tag tag1, Tag tag2) {
                if (tag1.getTagName() == tag2.getTagName()) {
                    return tag1.getCreatedDate().compareTo(tag2.getCreatedDate());
                }else{
                    return tag1.getTagName().compareTo(tag2.getTagName());
                }
            }
        });
        this.tagList.addAll(tags);
    }

    public void changeTitle(String title){this.title = title;}
    public void changeSlug(String slug){this.slug = slug;}
    public void changeBody(String body){this.body = body;}
    public void changeDescription(String description){this.description = description;}

}
