package com.inbatamilan.BlogAppScaler.security;

import com.inbatamilan.BlogAppScaler.users.UserEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.List;

public class JwtAutentication implements Authentication {

    String jwt;
    UserEntity userEntity;

    public JwtAutentication(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Returns the credential of the {@code Authentication} request.
     * For, eg, the password, or the Bearer token, or the cookie
     * @return
     */
    @Override
    public @Nullable String getCredentials() {
        return jwt;
    }

    @Override
    public @Nullable Object getDetails() {
        return null;
    }

    /**
     * Returns the principal of the {@code Authentication} request.
     * The "principal" is the entity that is being authenticated
     * In this case it is the User.
     * @return
     */
    @Override
    public @Nullable UserEntity getPrincipal() {
        return userEntity;
    }

    @Override
    public boolean isAuthenticated() {
        return (userEntity != null);
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    public String getName() {
        return "";
    }

    public boolean implies(Subject subject) {
        return Authentication.super.implies(subject);
    }
}
