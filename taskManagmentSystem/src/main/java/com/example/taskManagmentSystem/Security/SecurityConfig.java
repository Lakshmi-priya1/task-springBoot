package com.example.taskManagmentSystem.Security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

     
    @Value("${app.cors.allowed-origin}")
    private String allowedOrigin;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtFilter jwtFilter) throws Exception {

        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
    .requestMatchers(
        "/auth/login",
        "/auth/register",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    ).permitAll()
                .requestMatchers("/users/**").hasRole("ADMIN")

    // ADMIN and PM can create/delete projects
    .requestMatchers(HttpMethod.POST, "/projects/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")
    .requestMatchers(HttpMethod.DELETE, "/projects/**").hasRole("ADMIN")
    .requestMatchers(HttpMethod.GET, "/projects/**").authenticated()

    // ADMIN, PM, TEAM_LEAD can manage milestones
    .requestMatchers("/milestones/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "TEAM_LEAD")

    // Everyone logged in can update task status
    .requestMatchers(HttpMethod.PATCH, "/tasks/*/status").authenticated()
    .requestMatchers("/tasks/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "TEAM_LEAD")

    .anyRequest().authenticated()
)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(allowedOrigin));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
