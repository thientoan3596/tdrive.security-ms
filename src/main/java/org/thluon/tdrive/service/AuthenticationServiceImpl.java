package org.thluon.tdrive.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thluon.tdrive.dto.AuthenticationRequestDTO;
import org.thluon.tdrive.dto.AuthenticationResponseDTO;
import org.thluon.tdrive.dto.RegistrationRequestDTO;
import org.thluon.tdrive.entity.UserDetailsImpl;
import org.thluon.tdrive.exception.UniqueKeyViolationException;
import org.thluon.tdrive.mapper.UserMapper;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final ReactiveUserDetailsServiceImpl reactiveUserDetailsService;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public Mono<AuthenticationResponseDTO> register(@NonNull RegistrationRequestDTO request) throws UniqueKeyViolationException {
        UserDetailsImpl userDetails =  userMapper.toUserDetails(request);
        userDetails.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        return reactiveUserDetailsService.insertUser(userDetails)
                .map(this::buildAuthenticationResponse);
    }

    public Mono<AuthenticationResponseDTO> authenticate(@NonNull AuthenticationRequestDTO request) {
        //TODO: Update revocation user
        return reactiveAuthenticationManager
                .authenticate(request.toUsernamePasswordAuthenticationToken())
                .onErrorResume(LockedException.class, e ->
                        reactiveUserDetailsService.findByUsername(request.getEmail())
                                .cast(UserDetailsImpl.class)
                                .flatMap(user -> {
                                    String reason = user.getLockReason();
                                    return Mono.error(new LockedException(reason == null || reason.isBlank()
                                            ? "Tài khoản của bạn đã bị khóa! Vui lòng liên hệ quản trị viên!" : reason));
                                })
                                .then(Mono.error(e))
                )
                .cast(Authentication.class)
                .map(auth -> (UserDetailsImpl) auth.getPrincipal())
                .map(this::buildAuthenticationResponse);
    }

    @Override
    public Mono<AuthenticationResponseDTO> refreshToken(ServerHttpRequest request)
    throws BadCredentialsException, LockedException, ExpiredJwtException {
        final String  authHeader = request.getHeaders().getFirst("Authorization");
        //TODO: Check revocation
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            //TODO: exception handler
            throw new BadCredentialsException("Missing or Invalid Authorization Header");
        }
        final String refreshToken = authHeader.substring("Bearer ".length());
        final Claims claims;
        try {
            claims = jwtService.extractAllClaims(refreshToken);
        } catch (MalformedJwtException e) {
            throw new BadCredentialsException("Malformed Token");
        }
        //throw ExpiredJwtException
        if (claims == null) {
            throw new BadCredentialsException("Invalid Token");
        }
        UserDetails userDetails = UserDetailsImpl.builder()
                .email(claims.getSubject())
                .name((String) claims.get("name"))
                .role((String) claims.get("role"))
                .id(UUID.fromString((String) claims.get("id")))
                .build();
        return Mono.just(buildAuthenticationResponse(userDetails));
    }

    private AuthenticationResponseDTO buildAuthenticationResponse(@NonNull UserDetails user) {
        System.out.println(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
