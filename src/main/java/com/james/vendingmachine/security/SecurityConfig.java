package com.james.vendingmachine.security;

import com.james.vendingmachine.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers( "/api/v1/auth/**", "/api/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/product/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/product/**").hasAnyAuthority(Role.SELLER.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/product/**").hasAnyAuthority(Role.SELLER.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/product/**").hasAnyAuthority(Role.SELLER.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/purchase/**").hasAnyAuthority(Role.BUYER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/purchase/**").hasAnyAuthority(Role.BUYER.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/deposit**").hasAnyAuthority(Role.BUYER.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
