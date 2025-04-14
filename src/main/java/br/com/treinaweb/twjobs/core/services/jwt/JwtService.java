package br.com.treinaweb.twjobs.core.services.jwt;

import br.com.treinaweb.twjobs.core.enums.Role;

public interface JwtService {

    String generateAccessToken(String sub, Role role);
    String getSubFromAccessToken(String token);
    String generateRefreshToken(String sub, Role role);
    String getSubFromRefreshToken(String token);

}