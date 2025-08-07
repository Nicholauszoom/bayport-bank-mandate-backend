package com.example.Project.Management.bayport.annotation;

import com.example.Project.Management.bayport.auth.enumerated.Permission;
import com.example.Project.Management.bayport.auth.enumerated.Role;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class CustomAnnotations {
    // Custom Annotations for Authorization
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("@authorizationService.hasPermission(authentication.principal, T(com.example.Project.Management.bayport.auth.enumerated.Permission).{permission})")
    public @interface RequirePermission {
        Permission value();
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAnyAuthority({roles})")
    public @interface RequireRole {
        Role[] value();
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("@authorizationService.canAccessProject(authentication.principal, project)")
    public @interface RequireProjectAccess {
    }
}
