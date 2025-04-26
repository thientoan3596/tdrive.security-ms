package org.thluon.tdrive.service;

import com.github.thientoan3596.exception.UniqueKeyViolationException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.NonNull;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.thluon.tdrive.dto.AuthenticationRequestDTO;
import org.thluon.tdrive.dto.AuthenticationResponseDTO;
import org.thluon.tdrive.dto.RegistrationRequestDTO;
import reactor.core.publisher.Mono;

public interface AuthenticationService {
    default Mono<AuthenticationResponseDTO> register(@NonNull RegistrationRequestDTO request) throws UniqueKeyViolationException {
        throw new IllegalStateException("Not yet implemented");
    }

    default Mono<AuthenticationResponseDTO> refreshToken(ServerHttpRequest request)
            throws BadCredentialsException, LockedException, ExpiredJwtException {
        throw new IllegalStateException("Not yet implemented");
    }

    default Mono<AuthenticationResponseDTO> authenticate(@NonNull AuthenticationRequestDTO request) {
        throw new IllegalStateException("Not yet implemented");
    }

}

