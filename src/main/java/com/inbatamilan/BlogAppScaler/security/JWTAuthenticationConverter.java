package com.inbatamilan.BlogAppScaler.security;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class JWTAuthenticationConverter implements AuthenticationConverter {

    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {

        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return null;
        }

        var jwt = authHeader.substring(7);
        return  new JwtAutentication(jwt);
    }
}
