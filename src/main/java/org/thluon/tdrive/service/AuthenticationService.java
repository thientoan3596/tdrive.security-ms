package org.thluon.tdrive.service;

import lombok.NonNull;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.thluon.tdrive.dto.AuthenticationRequestDTO;
import org.thluon.tdrive.dto.AuthenticationResponseDTO;
import org.thluon.tdrive.dto.RegistrationRequestDTO;
import org.thluon.tdrive.exception.UniqueKeyViolationException;
import reactor.core.publisher.Mono;

public interface AuthenticationService {
    default Mono<AuthenticationResponseDTO> register(@NonNull RegistrationRequestDTO request) throws UniqueKeyViolationException{
        throw new IllegalStateException("Not yet implemented");
    }

    default Mono<AuthenticationResponseDTO> refreshToken(ServerHttpRequest request) {
        throw new IllegalStateException("Not yet implemented");
    }
//    AuthenticationResponseDTO refreshToken(HttpServletRequest request) throws IOException, AuthenticationException, ExpiredJwtException;

    default Mono<AuthenticationResponseDTO> authenticate(@NonNull AuthenticationRequestDTO request) {
        throw new IllegalStateException("Not yet implemented");
    }

//    default AuthenticationResponseDTO updatePassword(@NonNull Long userId, @NonNull String newPassword){
//        throw new IllegalStateException("Not yet implemented");
//    }
}

