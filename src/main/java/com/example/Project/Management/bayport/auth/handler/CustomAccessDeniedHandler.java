package com.example.Project.Management.bayport.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
        private final ObjectMapper objectMapper;

        public CustomAccessDeniedHandler() {
            this.objectMapper = new ObjectMapper();
            this.objectMapper.registerModule(new JavaTimeModule());
        }

        @Override
        public void handle(
                HttpServletRequest request,
                HttpServletResponse response,
                AccessDeniedException accessDeniedException) throws IOException {

            // Get current authentication for logging
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = getUsername(authentication);
            String userRoles = getUserRoles(authentication);
            String requiredRole = extractRequiredRole(request.getRequestURI());

            // Enhanced logging
            log.warn("Access denied for user '{}' with roles [{}] trying to access {} (requires: {}): {}",
                    username, userRoles, request.getRequestURI(), requiredRole, accessDeniedException.getMessage());

            // Set response status and headers
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            // Create comprehensive error response
            Map<String, Object> errorResponse = createErrorResponse(
                    request, authentication, username, userRoles, requiredRole, accessDeniedException);

            // Write JSON response
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            response.getWriter().flush();
        }

        private String getUsername(Authentication authentication) {
            if (authentication == null || !authentication.isAuthenticated()) {
                return "anonymous";
            }
            return authentication.getName();
        }

    private String getUserRoles(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            log.info("Authentication object is null");
            return "[]";
        }
        log.info("Authentication object is: {}", authentication.getAuthorities());
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ", "[", "]"));
    }


    private String extractRequiredRole(String requestURI) {
            // Extract expected role based on URI pattern
            if (requestURI.startsWith("/admin/")) {
                return "ADMIN";
            } else if (requestURI.startsWith("/manager/")) {
                return "MANAGER";
            } else if (requestURI.startsWith("/user/")) {
                return "USER";
            }
            return "AUTHENTICATED";
        }

        private Map<String, Object> createErrorResponse(
                HttpServletRequest request,
                Authentication authentication,
                String username,
                String userRoles,
                String requiredRole,
                AccessDeniedException accessDeniedException) {

            Map<String, Object> errorResponse = new HashMap<>();

            // Basic error information
            errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            errorResponse.put("status", HttpServletResponse.SC_FORBIDDEN);
            errorResponse.put("error", "Forbidden");
            errorResponse.put("path", request.getRequestURI());

            // User information
            errorResponse.put("user", username);
            errorResponse.put("userRoles", userRoles);

            // Detailed message
            if (authentication != null && authentication.isAuthenticated()) {
                errorResponse.put("message", "Access denied - insufficient privileges");
                errorResponse.put("details", String.format(
                        "User '%s' with roles %s does not have required role '%s' for this resource",
                        username, userRoles, requiredRole));
            } else {
                errorResponse.put("message", "Authentication required");
                errorResponse.put("details", "Please provide valid authentication credentials");
            }

            // Additional debugging information (remove in production)
            if (isDebugMode()) {
                errorResponse.put("requestMethod", request.getMethod());
                errorResponse.put("requestHeaders", getRequestHeaders(request));
                errorResponse.put("requiredRole", requiredRole);
                errorResponse.put("exceptionMessage", accessDeniedException.getMessage());
            }

            return errorResponse;
        }

        private boolean isDebugMode() {
            // You can make this configurable via application properties
            return log.isDebugEnabled();
        }

        private Map<String, String> getRequestHeaders(HttpServletRequest request) {
            Map<String, String> headers = new HashMap<>();
            request.getHeaderNames().asIterator().forEachRemaining(
                    headerName -> headers.put(headerName, request.getHeader(headerName))
            );
            // Don't include sensitive headers in production
            headers.remove("authorization");
            headers.remove("cookie");
            return headers;
        }
}
