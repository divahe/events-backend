package com.example.events.service;

import com.example.events.dto.AuthResponseDto;
import com.example.events.dto.LoginDto;
import com.example.events.enums.TokenType;
import com.example.events.exceptions.FailedAuthenticationException;
import com.example.events.exceptions.InvalidTokenException;
import com.example.events.exceptions.UserNotFoundException;
import com.example.events.models.AppUser;
import com.example.events.models.Token;
import com.example.events.repository.AppUserRepository;
import com.example.events.repository.TokenRepository;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository repository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponseDto authenticate(LoginDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new FailedAuthenticationException("Invalid credentials");
        }
        var user = repository.findByUsername(request
                .getUsername())
                .orElseThrow(() -> new FailedAuthenticationException("Invalid credentials"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        saveUserToken(user, refreshToken);
        return AuthResponseDto
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(AppUser user, String jwtToken) {
        var token = Token
                .builder()
                .appUserId(user
                        .getId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(AppUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthResponseDto refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Authorization header missing or malformed");
        }
        refreshToken = authHeader.substring(7);
        userName = jwtService.extractUserName(refreshToken);
        if (userName != null) {
            var user = this.repository.findByUsername(userName)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                saveUserToken(user, accessToken);
                return AuthResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
            }
        }
        throw new InvalidTokenException("Invalid refresh token");
    }
}
