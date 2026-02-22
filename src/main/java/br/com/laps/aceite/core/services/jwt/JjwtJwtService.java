package br.com.laps.aceite.core.services.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JjwtJwtService implements JwtService {

    private final JwtConfigProperties configProperties;

    @Override
    public String generateAccessToken(String sub, Role role) {
        return generateToken(
                sub,
                role,
                configProperties.getAccessSecret(),
                configProperties.getAccessExpiresIn()
        );
    }

    @Override
    public String getSubFromAccessToken(String token) {
        return getSubFromToken(token, configProperties.getAccessSecret());
    }

    @Override
    public String generateRefreshToken(String sub, Role role) {
        return generateToken(
                sub,
                role,
                configProperties.getRefreshSecret(),
                configProperties.getRefreshExpiresIn()
        );
    }

    @Override
    public String getSubFromRefreshToken(String token) {
        return getSubFromToken(token, configProperties.getRefreshSecret());
    }

    private String generateToken(String sub, Role role, String secret, Long expiresIn) {
        var now = Instant.now();
        var expiration = now.plusSeconds(expiresIn);
        var key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setSubject(sub)
                .claim("role", role.name()) // Adicionando a role ao claim do token
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }

    private String getSubFromToken(String token, String secret) {
        var key = Keys.hmacShaKeyFor(secret.getBytes());
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            throw new JwtServiceException("Erro ao decodificar o token JWT: " + e.getMessage());
        }
    }
}