package com.io.realworld.repository;

import lombok.Builder;

import javax.persistence.*;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;
    private String password;

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
