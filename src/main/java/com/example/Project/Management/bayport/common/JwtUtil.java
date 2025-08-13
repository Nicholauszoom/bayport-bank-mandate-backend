package com.dflex.emkopo.commons.utils;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class JwtUtil {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final JwtParser jwtParser;

    @Autowired
    public JwtUtil(PKIUtils pkiUtils) throws Exception {
        this.privateKey = pkiUtils.getPrivateKey();
        this.publicKey = pkiUtils.getPublicKey();
        log.info("Token validation key pair has been set.");
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.publicKey).build();
    }

    public String createToken(String subject, Map<String, Object> details, int minutes) {
        log.info("User with id {} requested a token with claims {}.", subject, details);

        Claims claims = Jwts.claims().setSubject(subject);
        claims.putAll(details);

        Date tokenCreationTime  = new Date();
        Date tokenValidity      = new Date(tokenCreationTime.getTime() + TimeUnit.MINUTES.toMillis(minutes));
        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer("DFlex International Limited")
                .setIssuedAt(tokenCreationTime)
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(this.privateKey)
                .compact();
        log.info("A token has been issued to user with id {}.", subject);
        return token;
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(String token) {
        if (token != null) {
            return parseJwtClaims(token);
        }
        return null;
    }

    public String resolveToken(HttpServletRequest request) {
        String TOKEN_HEADER = "Authorization";
        String bearerToken = request.getHeader(TOKEN_HEADER);
        String TOKEN_PREFIX = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(Objects.equals(cookie.getName(), "X-CSRF-TOKEN")){
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}