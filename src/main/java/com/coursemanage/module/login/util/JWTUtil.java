package com.coursemanage.module.login.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JWTUtil {
    private static final String SECRET_KET_STRING = "my-super-secret-demo-key-2025-111101";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KET_STRING.getBytes());
    private static final long EXPIRATION_TIME = 365 * 24 * 60 * 60 * 1000L;
    public static String generateToken(Map<String, Object> claims){
        JwtBuilder jwtBuilder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY);
        return jwtBuilder.compact();
    }
    public static String generateToken(Map<String, Object> claims, Long expirationTime){
        JwtBuilder jwtBuilder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY);
        return jwtBuilder.compact();
    }
    public static Claims getClaimsFromToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
        return claimsJws.getPayload();
    }
    public static Date getExpirationFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }
    private static boolean isTokenExpired(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }
    public static void validateToken(String token) {
        getClaimsFromToken(token);
    }
}
