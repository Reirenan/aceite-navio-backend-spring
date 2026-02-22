package br.com.laps.aceite.api.auth.controllers;

import br.com.laps.aceite.api.auth.dtos.*;


import br.com.laps.aceite.api.auth.mappers.UserMapper;
import br.com.laps.aceite.core.permissions.PortoUsersPermissions;
import br.com.laps.aceite.core.repositories.UserRepository;
import br.com.laps.aceite.core.services.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PortoUsersPermissions.IsAdministrador
    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid UserRequest userRequest) {
        var user = userMapper.toUser(userRequest);
        var passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.getEmail());
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return TokenResponse.builder()
                .accessToken(jwtService.generateAccessToken(loginRequest.getEmail(), user.get().getRole()))
                .refreshToken(jwtService.generateRefreshToken(loginRequest.getEmail(), user.get().getRole()))
                .build();
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody @Valid RefreshRequest refreshRequest) {
        var sub = jwtService.getSubFromRefreshToken(refreshRequest.getRefreshToken());
        var user = userRepository.findByEmail(sub);
        return TokenResponse.builder()
                .accessToken(jwtService.generateAccessToken(sub, user.get().getRole()))
                .refreshToken(jwtService.generateRefreshToken(sub, user.get().getRole()))
                .build();
    }
}