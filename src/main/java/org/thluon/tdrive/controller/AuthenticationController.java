package org.thluon.tdrive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.thluon.tdrive.dto.AuthenticationRequestDTO;
import org.thluon.tdrive.dto.AuthenticationResponseDTO;
import org.thluon.tdrive.dto.RegistrationRequestDTO;
import org.thluon.tdrive.service.AuthenticationService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${application.api.endpoint}/${application.api.version}")
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationAPI {
    private final AuthenticationService authenticationService;

    @Override
    public Mono<ResponseEntity<AuthenticationResponseDTO>>register(Mono<RegistrationRequestDTO> requestMono) {
        return requestMono
                .flatMap(request -> {
                    if (request.getPassword() != null && !request.getPassword().equals(request.getPasswordConfirm())) {
                        BindingResult result = new BeanPropertyBindingResult(request, "registrationRequestDTO");
                        result.rejectValue("passwordConfirm", "", "Mật khẩu xác nhận không khớp");
                        MethodParameter param;
                        try {
                            param = new MethodParameter(
                                    this.getClass().getDeclaredMethod("register", Mono.class), 0
                            );
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                        return Mono.error(new WebExchangeBindException(param, result));
                    }
                    return authenticationService.register(request).map(ResponseEntity::ok);
                });
    }

    @Override
    public Mono<ResponseEntity<AuthenticationResponseDTO>> authenticate(AuthenticationRequestDTO authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AuthenticationResponseDTO>> refreshToken(ServerHttpRequest request) {
        return authenticationService.refreshToken(request).map(ResponseEntity::ok);
    }
}
