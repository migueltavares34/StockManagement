package com.example.demo.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;
    private final Object principal;

    public JwtAuthenticationToken(String token) {
        super(Collections.EMPTY_LIST);
        this.token = token;
        this.principal = null;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(Object principal, String token, 
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

