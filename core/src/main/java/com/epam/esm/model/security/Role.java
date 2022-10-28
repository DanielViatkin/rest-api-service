package com.epam.esm.model.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
    GUEST(Stream.of(Permission.TAG_GET,
            Permission.USER_CREATE,
            Permission.ORDER_GET,
            Permission.CERTIFICATE_GET,
            Permission.LOGIN, Permission.SIGNUP).collect(Collectors.toSet())),
    USER(Stream.of(Permission.TAG_GET,
            Permission.USER_GET, Permission.USER_UPDATE,
            Permission.ORDER_GET, Permission.ORDER_CREATE,
            Permission.CERTIFICATE_GET,
            Permission.LOGOUT,
            Permission.CHANGE_PASSWORD).collect(Collectors.toSet())),
    ADMIN(Stream.of(Permission.TAG_GET, Permission.TAG_CREATE, Permission.TAG_UPDATE, Permission.TAG_DELETE,
            Permission.USER_GET, Permission.USER_UPDATE, Permission.USER_CREATE, Permission.USER_DELETE,
            Permission.ORDER_GET, Permission.ORDER_CREATE,
            Permission.CERTIFICATE_GET, Permission.CERTIFICATE_CREATE, Permission.CERTIFICATE_UPDATE, Permission.CERTIFICATE_DELETE,
            Permission.LOGOUT,
            Permission.CHANGE_PASSWORD).collect(Collectors.toSet()));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions){
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions(){
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
