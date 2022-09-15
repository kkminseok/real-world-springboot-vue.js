package com.io.realworld.security.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("classpath:application.properties")
public class JwtConfig {

    @Value("${real-world.token.expiry}")
    private Long expiry;

    @Value("${real-world.token.key}")
    private String key;
}
