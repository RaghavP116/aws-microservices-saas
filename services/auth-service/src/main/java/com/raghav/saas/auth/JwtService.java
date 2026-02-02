package com.raghav.saas.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.expirationMinutes}")
    private long expirationMinutes;

    public String createToken(AppUser user) {
        var now = Instant.now();
        var exp = now.plusSeconds(expirationMinutes * 60);

        var key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(user.userId())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("tenantId", user.tenantId())
                .claim("roles", user.roles())
                .claim("email", user.email())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
