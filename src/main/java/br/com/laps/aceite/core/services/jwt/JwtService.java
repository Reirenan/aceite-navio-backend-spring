package br.com.laps.aceite.core.services.jwt;


import br.com.laps.aceite.core.enums.Role;

public interface JwtService {

    String generateAccessToken(String sub, Role role);
    String getSubFromAccessToken(String token);
    String generateRefreshToken(String sub, Role role);
    String getSubFromRefreshToken(String token);

}