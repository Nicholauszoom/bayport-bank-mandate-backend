package com.example.Project.Management.bayport.auth.principal;

import com.example.Project.Management.bayport.auth.enumerated.Role;
import com.example.Project.Management.bayport.auth.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CustomUserPrincipal implements UserDetails {

    private final User user;

    public CustomUserPrincipal(User user) {
        this.user = Objects.requireNonNull(user, "User cannot be null");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security requires ROLE_ prefix
        String roleName = user.getRole().name();
        return Collections.singleton(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Using email as username for JWT
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement based on your business logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement based on your business logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement based on your business logic
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement based on your User entity
    }

    // Convenience methods
    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String toString() {
        return "CustomUserPrincipal{" +
                "userId=" + user.getId() +
                ", username='" + user.getUsername() + '\'' +
                ", email='" + user.getEmail() + '\'' +
                ", role=" + user.getRole() +
                '}';
    }
}