package br.com.laps.aceite.core.services.jwt;



public interface JwtService {

    String generateAccessToken(String sub, Role role);
    String getSubFromAccessToken(String token);
    String generateRefreshToken(String sub, Role role);
    String getSubFromRefreshToken(String token);

}