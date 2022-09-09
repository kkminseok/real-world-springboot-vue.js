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

    @Builder
    public User(String username, String email, String password, String bio, String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.image = image;
    }

    public static User of(String username, String email, String password) {
        return new User(username, email, password, "", "");
    }

    protected User() {
    }

}
