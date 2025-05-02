package com.example.shoeshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;

@Configuration
public class ActuatorSecurityConfig {

    @Bean
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/actuator/health/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/actuator/info")).permitAll()
                        .anyRequest().hasRole("ADMIN")
                );

        return http.build();
    }
}
