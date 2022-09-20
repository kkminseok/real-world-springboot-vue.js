package com.io.realworld.config;

import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import com.io.realworld.domain.aggregate.user.entity.User;
import com.io.realworld.domain.aggregate.user.service.UserServiceDetail;
import com.io.realworld.domain.service.JwtService;
import com.io.realworld.security.jwt.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        Long id = annotation.id();
        String email =  annotation.email();
        String username = annotation.username();

        String token = this.createToken(email);
        System.out.println("token" + token);
        Authentication auth = this.getAuthentication(token);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        return context;

    }

}
