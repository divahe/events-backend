package com.example.events.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static com.example.events.enums.Role.ADMIN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Admin uses JWT, so no extra config is needed
                .cors(cors -> cors
                        .configurationSource(request -> {
                            var corsConfig = new CorsConfiguration();
                            corsConfig.setAllowedOrigins(List.of("http://localhost:5173"));
                            corsConfig.setAllowedMethods(List.of("GET", "PUT", "POST", "OPTIONS"));
                            corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                            corsConfig.setAllowCredentials(true);
                            corsConfig.addExposedHeader("Authorization");
                            var source = new UrlBasedCorsConfigurationSource();
                            source.registerCorsConfiguration("/api/v1/auth/logout", corsConfig);
                            return corsConfig;
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/v1/auth/**").permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers("api/v1/events/**").permitAll()
                        .requestMatchers("api/v1/admin/**").hasAnyRole(ADMIN.name())
                        .anyRequest()
                        .authenticated()

                )
                //.headers(AbstractHttpConfigurer::disable) // for exposing  H2 console data
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;
        return http.build();
    }
}
