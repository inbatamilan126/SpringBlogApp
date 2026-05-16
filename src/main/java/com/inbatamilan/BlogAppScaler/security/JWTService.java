package com.inbatamilan.BlogAppScaler.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    // TODO: Move the key to a separate .properties file (not in git)
    private static final String JWT_KEY = "dkljjlsdjfkldoijlkjfkjrlejrljeldkfm";

    private final Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);

    public String createJwt(Long userId) {
        return JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(new Date())
                //.withExpiresAt() // TODO: Setup expiry
                .sign(algorithm);
    }

    public Long retrieveUserId(String jwtString) {
        var decodedJwt = JWT.decode(jwtString);
        var userId = Long.valueOf(decodedJwt.getSubject());
        return userId;
    }
}
