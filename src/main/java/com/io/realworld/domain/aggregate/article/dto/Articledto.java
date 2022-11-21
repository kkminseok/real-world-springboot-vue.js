package com.io.realworld.domain.aggregate.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.io.realworld.config.TagListDeserializer;
import lombok.*;


import java.text.ParseException;
import java.util.List;

@Builder
@Getter
@ToString
@AllArgsConstructor
@JsonTypeName("article")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class Articledto {
    private String title;
    private String description;
    private String body;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    //@JsonDeserialize(using = TagListDeserializer.class)
    private List<String> tagList;


}
