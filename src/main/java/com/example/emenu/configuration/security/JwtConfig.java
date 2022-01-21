package com.example.emenu.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Date;
import java.util.stream.Collectors;

@Getter
public class JwtConfig {
    @Value("${security.jwt.uri}")
    private String uri;

    @Value("${security.jwt.header}")
    private String header;

    @Value("${security.jwt.prefix}")
    private String prefix;

    @Value("${security.jwt.expiration}")
    private long expiration;

    @Value("${security.jwt.secret}")
    private String secret;

    public String buildJwtToken(Authentication auth) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(auth.getName())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities",
                        auth.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + this.getExpiration()))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512,
                        this.getSecret()
                                .getBytes())
                .compact();
    }

    public Claims validateJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(this.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}