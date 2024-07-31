package com.esab.springauth.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.esab.springauth.entities.User;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.UUID;

@Service
public class TokenProvider {
  @Value("${security.jwt.token.secret-key}")
  private String JWT_SECRET;

  private Algorithm getAlgorithm() {
    byte[] decodedSecret = Base64.getDecoder().decode(JWT_SECRET);
    return Algorithm.HMAC512(decodedSecret);
  }

  public String generateAccessToken(User user) {
    try {
      Algorithm algorithm = getAlgorithm();
      String token = JWT.create()
          .withSubject(user.getUsername())
          .withClaim("username", user.getUsername())
          .withExpiresAt(genAccessExpirationDate())
          .withJWTId(UUID.randomUUID().toString())
          .sign(algorithm);

      return token;
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Error while generating token: " + exception.getMessage(), exception);
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = getAlgorithm();
      JWTVerifier verifier = JWT.require(algorithm)
          .acceptExpiresAt(30) // Optional: Grace period for expiration
          .build();
      DecodedJWT decodedJWT = verifier.verify(token);

      return decodedJWT.getSubject();
    } catch (JWTVerificationException exception) {
      throw new RuntimeException("Error while validating token: " + exception.getMessage(), exception);
    }
  }

  private Instant genAccessExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC); // Use UTC or your desired timezone
  }
}