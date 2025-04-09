package org.thluon.tdrive.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    default <T> T extractClaim(@NonNull String token, @NonNull Function<Claims, T> claimResolver)
            throws ExpiredJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, NullPointerException {
        throw new IllegalStateException("Not implemented");
    }

    default String generateToken(@NonNull Map<String, Object> extraClaims, @NonNull UserDetails userDetails) {
        throw new IllegalStateException("Not implemented");
    }

    default String generateToken(UserDetails userDetails) {
        throw new IllegalStateException("Not implemented");
    }

    default String generateRefreshToken(@NonNull UserDetails userDetails) {
        throw new IllegalStateException("Not implemented");
    }

   default Claims extractAllClaims(@NonNull String token)
            throws ExpiredJwtException, MalformedJwtException, SignatureException,IllegalArgumentException,NullPointerException{
       throw new IllegalStateException("Not implemented");
   }
}
