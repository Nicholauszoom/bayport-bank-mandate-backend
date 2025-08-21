package com.example.Project.Management.bayport.auth.user.service;

import com.example.Project.Management.bayport.auth.principal.CustomUserPrincipal;
import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by username/email: {}", email);

        try {
            // Try to find by email first, then by username
            User user = userRepository.findByEmail(email)
                    .or(() -> userRepository.findByUsername(email)) // Fix: try username, not email again
                    .orElseThrow(() -> {
                        log.warn("User not found: {}", email);
                        return new UsernameNotFoundException("User not found: " + email);
                    });

            log.debug("Successfully loaded user: {} with role: {}",
                    user.getEmail(), user.getRole());

            return new CustomUserPrincipal(user);

        } catch (Exception e) {
            log.error("Error loading user {}: {}", email, e.getMessage());
            throw new UsernameNotFoundException("Error loading user: " + email, e);
        }
    }
}