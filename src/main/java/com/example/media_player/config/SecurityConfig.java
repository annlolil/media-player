package com.example.media_player.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // allow H2 console
                        .anyRequest().authenticated()                  // all other requests need a token
                )
                .csrf(csrf -> csrf.disable())                     // needed for H2 console
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // allow frames for H2
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> {}) // enables JWT decoding without using the deprecated method
                );

        return http.build();
    }
}
