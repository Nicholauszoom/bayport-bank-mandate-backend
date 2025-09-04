package com.example.Project.Management.bayport.config;

import com.example.Project.Management.bayport.auth.handler.CustomAccessDeniedHandler;
import com.example.Project.Management.bayport.auth.handler.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/profile**", "/user**", "/dashboard", "/verify", "/mandate-list**", "/create-mandate**","/upload-mandate**", "/view-mandate/**").permitAll()

                        .requestMatchers("/static/**", "/media/**", "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()

                        .requestMatchers("/*.js", "/*.css", "/*.png", "/*.jpg", "/*.jpeg", "/*.gif", "/*.svg", "/*.ico").permitAll()

                        .requestMatchers("/main-*.js", "/chunk-*.js", "/polyfills-*.js", "/styles-*.css").permitAll()

                        .requestMatchers("/favicon.ico", "/error", "/index.html").permitAll()

                        .requestMatchers("/api/auth/**", "/public/**", "/health","/api/loans/message/**").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/user/**", "/api/charge/**", "/api/direct/debit/mandate/**").hasAnyAuthority("ADMIN", "MANAGER")

                        .requestMatchers(HttpMethod.GET, "/api/product/**").hasAnyAuthority("USER", "MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/product/create").hasAnyAuthority("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/product/**").hasAnyAuthority("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/product/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/user/detail").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }

    // for manage local environment
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    // for manage test and prod environmnet
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Add more potential origins
        configuration.setAllowedOrigins(List.of(
                "http://10.25.39.90:8443",
                "http://localhost:8443",
                "http://127.0.0.1:8443"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}