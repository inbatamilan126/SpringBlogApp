package com.inbatamilan.BlogAppScaler.security;

import com.inbatamilan.BlogAppScaler.users.UsersService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JWTAuthenticationManager implements AuthenticationManager {

    private JWTService jwtService;
    private UsersService usersService;

    public JWTAuthenticationManager(JWTService jwtService, UsersService usersService) {
        this.jwtService = jwtService;
        this.usersService = usersService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        if (authentication instanceof JwtAutentication jwtAuthentication) {
            var jwt = jwtAuthentication.getCredentials();
            var userId = jwtService.retrieveUserId(jwt);
            var userEntity = usersService.getUser(userId);

            jwtAuthentication.userEntity = userEntity;
            jwtAuthentication.setAuthenticated(true);

            return jwtAuthentication;
        }

        throw new IllegalAccessError("Cannot authenticate with non-JWT authentication");
    }
}
