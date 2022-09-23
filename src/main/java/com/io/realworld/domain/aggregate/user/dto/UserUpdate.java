package com.io.realworld.domain.aggregate.user.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;

@Builder
@Getter
@Setter
@ToString
@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UserUpdate {

    private Long id;
    @Email
    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;
}
