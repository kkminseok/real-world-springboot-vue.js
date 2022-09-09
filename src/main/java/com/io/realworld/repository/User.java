package com.io.realworld.repository;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Table(name = "users")
@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;
    private String password;

    private String bio;
    private String image;
    private String token;

    @Builder
    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static User of(String username, String email, String password){
        return new User(username,email,password);
    }

    protected User(){
    }

}
