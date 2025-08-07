package com.example.Project.Management.bayport.config;

import com.example.Project.Management.bayport.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");

        // Skip JWT processing for public endpoints
        if (isPublicEndpoint(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check if Authorization header is present and valid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No valid Authorization header found for request: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract JWT token (remove "Bearer " prefix)
            final String jwt = authHeader.substring(7);
            log.debug("Processing JWT token for request: {}", requestURI);

            // Extract username from token
            final String userEmail = jwtService.extractUsername(jwt);

            // Get current authentication from security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Process token if username exists and no authentication is set
            if (userEmail != null && authentication == null) {
                log.debug("Validating token for email: {}", userEmail);

                // Validate token
                if (jwtService.isTokenValid(jwt, userEmail)) {
                    log.debug("JWT token is valid for user: {}", userEmail);

                    // Extract roles from token
                    List<String> roles = jwtService.extractClaim(jwt, claims -> claims.get("roles", List.class));
                    if (roles == null || roles.isEmpty()) {
                        log.warn("No roles found in JWT for user: {}", userEmail);
                        throw new IllegalStateException("No roles found in JWT");
                    }

                    // Convert roles to GrantedAuthority
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    log.debug("Extracted authorities from JWT: {}", authorities);

                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail, // Use email as principal
                            null,      // No credentials needed
                            authorities
                    );

                    // Set additional details
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.debug("Successfully authenticated user: {} for request: {}", userEmail, requestURI);
                } else {
                    log.warn("Invalid JWT token for user: {}", userEmail);
                    SecurityContextHolder.clearContext();
                }
            }

            // Continue filter chain
            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            log.error("JWT authentication failed for request {}: {}", requestURI, exception.getMessage(), exception);
            SecurityContextHolder.clearContext();

            // Handle the exception manually instead of using HandlerExceptionResolver
            handleAuthenticationException(response, exception);
        }
    }

    private void handleAuthenticationException(HttpServletResponse response, Exception exception) {
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String errorMessage = "Authentication failed";
            if (exception instanceof ExpiredJwtException) {
                errorMessage = "Token has expired";
            } else if (exception instanceof MalformedJwtException) {
                errorMessage = "Invalid token format";
            } else if (exception instanceof SignatureException) {
                errorMessage = "Invalid token signature";
            } else if (exception instanceof IllegalArgumentException) {
                errorMessage = "Token claims string is empty";
            }

            String jsonError = String.format("""
                {
                    "error": "Unauthorized",
                    "message": "%s",
                    "timestamp": "%s",
                    "status": 401
                }
                """, errorMessage, java.time.Instant.now().toString());

            response.getWriter().write(jsonError);
            response.getWriter().flush();

        } catch (IOException e) {
            log.error("Error writing authentication error response", e);
        }
    }

    private boolean isPublicEndpoint(String requestURI) {
        return requestURI.startsWith("/auth/") ||
                requestURI.startsWith("/public/") ||
                requestURI.equals("/health") ||
                requestURI.equals("/actuator/health");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/images/") ||
                path.startsWith("/webjars/") ||
                path.equals("/favicon.ico");
    }
}