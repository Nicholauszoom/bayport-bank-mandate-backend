package com.example.Project.Management.bayport.crdb.service;


import com.example.Project.Management.bayport.crdb.dto.security.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;

@Service
@Slf4j
public class TokenManagementService {

    @Value("${crdb.token.url}") // e.g., https://<ESB API GW IP>/oauth/token
    private String tokenUrl;

    @Value("${crdb.client.id}")
    private String clientId;

    @Value("${crdb.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    // Cached token and expiration time
    private String cachedToken;
    private Instant tokenExpirationTime;
    private final Object lock = new Object();

    public TokenManagementService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Gets a valid access token. If cached token is expired or null, requests a new one.
     */
    public String getValidToken() {
        synchronized (lock) {
            if (isTokenValid()) {
                log.info("Using cached token");
                return cachedToken;
            }

            log.info("Token expired or not available, requesting new token");
            return requestNewToken();
        }
    }

    /**
     * Checks if the cached token is valid (not null and not expired)
     */
    private boolean isTokenValid() {
        if (cachedToken == null || tokenExpirationTime == null) {
            return false;
        }

        // Add buffer time (e.g., 60 seconds before actual expiration)
        Instant bufferTime = Instant.now().plusSeconds(60);
        boolean isValid = tokenExpirationTime.isAfter(bufferTime);

        if (!isValid) {
            log.info("Token expired at: {}, current time: {}", tokenExpirationTime, Instant.now());
        }

        return isValid;
    }

    /**
     * Requests a new token from the external system
     */
    private String requestNewToken() {
        try {
            // Build URL with query parameters (matching your working Postman request)
            String tokenUrlWithParams = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                    .queryParam("grant_type", "client_credentials")
                    .queryParam("client_id", clientId)
                    .queryParam("client_secret", clientSecret)
                    .toUriString();

            log.info("Requesting new token from: {}", tokenUrlWithParams);

            // Prepare headers (matching your working Postman request)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Empty body (matching your working Postman request with --data '')
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    tokenUrlWithParams,
                    HttpMethod.POST,
                    entity,
                    TokenResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                TokenResponse tokenResponse = response.getBody();
                cachedToken = tokenResponse.getAccessToken();

                // Calculate expiration time (expires_in is usually in seconds)
                int expiresInSeconds = tokenResponse.getExpiresIn();
                tokenExpirationTime = Instant.now().plusSeconds(expiresInSeconds);

                log.info("New token received, expires at: {}", tokenExpirationTime);
                return cachedToken;
            } else {
                throw new RuntimeException("Failed to retrieve token, status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error requesting token: {}", e.getMessage(), e);
            throw new RuntimeException("Token request failed", e);
        }
    }

}