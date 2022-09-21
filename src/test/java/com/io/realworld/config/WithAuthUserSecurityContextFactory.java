package com.io.realworld.config;

import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;


public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        Long id = annotation.id();
        String email =  annotation.email();
        String username = annotation.username();

        UserAuth userAuth = UserAuth.builder()
                .email(email)
                .username(username)
                .id(id)
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userAuth,"",null);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        return context;

    }

}
