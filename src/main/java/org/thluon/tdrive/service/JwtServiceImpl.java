package org.thluon.tdrive.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.thluon.tdrive.entity.UserDetailsImpl;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService{
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.token.expiration:36_000_000}")
    private long expiration;
    @Value("${jwt.refresh-token.expiration:604_800_000}")
    private long refreshTokenExpiration;

    private SecretKey secretKey;
    @PostConstruct
    public void init(){
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    @Override
    public <T> T extractClaim(@NonNull String token, @NonNull Function<Claims, T> claimResolver)
            throws ExpiredJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, NullPointerException
    {
        final Claims claim = extractAllClaims(token);
        return claimResolver.apply(claim);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String,Object> claims = generalClaims(userDetails);
        return generateToken(claims,userDetails);
    }

    @Override
    public String generateToken(@NonNull Map<String, Object> extraClaims, @NonNull UserDetails userDetails) {
        return buildToken(extraClaims,userDetails,expiration);
    }

    @Override
    public String generateRefreshToken(@NonNull UserDetails userDetails) {
        Map<String,Object> claims = generalClaims(userDetails);
        return buildToken(claims,userDetails,refreshTokenExpiration);
    }

    private Map<String,Object> generalClaims(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        if(userDetails instanceof UserDetailsImpl userDetailsImpl){
            claims.put("email",userDetailsImpl.getEmail());
            claims.put("role",userDetailsImpl.getRole());
            claims.put("name",userDetailsImpl.getName());
            claims.put("id",userDetailsImpl.getId());
        }
        return claims;
    }
    private String buildToken(
            @NonNull Map<String, Object> extraClaims,
            @NonNull UserDetails userDetails,
            long expiration
    ){
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new java.util.Date(System.currentTimeMillis()))
                .expiration(new java.util.Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }
    @Override
    public Claims extractAllClaims(@NonNull String token)
            throws ExpiredJwtException, MalformedJwtException, SignatureException,IllegalArgumentException,NullPointerException
    {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
