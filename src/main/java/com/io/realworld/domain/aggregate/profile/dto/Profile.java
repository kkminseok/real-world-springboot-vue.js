package com.io.realworld.domain.aggregate.profile.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonTypeName("profile")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class Profile {
    private String username;
    private String bio;
    private String image;
    private Boolean following;

}

