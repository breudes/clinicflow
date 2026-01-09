package com.breudes.clinicflow.schedule.infra.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.breudes.clinicflow.schedule.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.secret}")
    private String apiSecret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("ClinicFlow API")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId())
                    .withIssuedAt(generateCreationDate())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }

    public String getSubject(String token) {
        if (token == null) {
            throw new RuntimeException();
        }
        DecodedJWT verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    .withIssuer("ClinicFlow API")
                    .build()
                    .verify(token);

            verifier.getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Invalid or expired JWT token");
        }
        if (verifier.getSubject() == null) {
            throw new RuntimeException("Verifier null");
        } else
            return verifier.getSubject();
    }

    private Instant generateCreationDate() {
        return LocalDateTime.now(ZoneId.of("GMT-3")).toInstant(ZoneOffset.of("-03:00"));
    }
    private Instant generateExpirationDate() {
        // You can change the 'GMT-3' based off your timezone
        // This token expires after 2 hours of created date
        return LocalDateTime.now(ZoneId.of("GMT-3")).plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}