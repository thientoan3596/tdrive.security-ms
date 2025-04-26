package org.thluon.tdrive.controller;

import com.github.thientoan3596.exception.UniqueKeyViolationException;
import jakarta.validation.constraints.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.thluon.tdrive.config.SecurityConf;
import org.thluon.tdrive.dto.AuthenticationRequestDTO;
import org.thluon.tdrive.dto.AuthenticationResponseDTO;
import org.thluon.tdrive.dto.RegistrationRequestDTO;
import org.thluon.tdrive.service.AuthenticationService;
import org.thluon.tdrive.utilities.ValidationMessageExtractor;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(controllers = AuthenticationController.class)
@Import(SecurityConf.class)
@TestPropertySource(properties = {
        "SPRINGDOC_SECURE=false",
})
class AuthenticationControllerTest {

    @MockBean
    AuthenticationService authenticationService;
    @Autowired
    private WebTestClient webClient;

    @Nested
    class Register {
        @Test
        void happyPath() {
            RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                    .name("Example")
                    .email("email@example.com")
                    .password("password")
                    .passwordConfirm("password")
                    .build();
            AuthenticationResponseDTO response = AuthenticationResponseDTO.builder().build();
            Mockito.when(authenticationService.register(request)).thenReturn(Mono.just(response));
            webClient.post()
                    .uri("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk();
            Mockito.verify(authenticationService, Mockito.times(1)).register(any());
        }

        @Nested
        class Validation {
            @Test
            void blankName() {
                RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                        .email("email@example.com")
                        .password("password")
                        .passwordConfirm("password")
                        .build();
                webClient.post()
                        .uri("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("NotBlank")
                        .jsonPath("$.errors[0].field").isEqualTo("name")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(RegistrationRequestDTO.class, "name", NotBlank.class));
            }
            @Test
            void shortName() {
                RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                        .name("S")
                        .email("email@example.com")
                        .password("password")
                        .passwordConfirm("password")
                        .build();
                webClient.post()
                        .uri("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("Size")
                        .jsonPath("$.errors[0].field").isEqualTo("name")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(RegistrationRequestDTO.class, "name", Size.class));
            }

            @Test
            void longName() {
                RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                        .name("long name".repeat(500))
                        .email("email@example.com")
                        .password("password")
                        .passwordConfirm("password")
                        .build();
                webClient.post()
                        .uri("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("Size")
                        .jsonPath("$.errors[0].field").isEqualTo("name")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(RegistrationRequestDTO.class, "name", Size.class));

            }
            @Test
            void nullEmail() {
                RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                        .name("Example")
                        .password("password")
                        .passwordConfirm("password")
                        .build();
                webClient.post()
                        .uri("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("NotNull")
                        .jsonPath("$.errors[0].field").isEqualTo("email")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(RegistrationRequestDTO.class, "email", NotNull.class));
            }
            @Test
            void invalidEmail() {
                RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                        .name("Example")
                        .email("email")
                        .password("password")
                        .passwordConfirm("password")
                        .build();
                webClient.post()
                        .uri("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("Email")
                        .jsonPath("$.errors[0].field").isEqualTo("email")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(RegistrationRequestDTO.class, "email", Email.class));
            }
            @Test
            void blankPassword() {
                RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                        .name("Example")
                        .email("email@example.com")
                        .password("")
                        .passwordConfirm("")
                        .build();
                webClient.post()
                        .uri("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("NotBlank")
                        .jsonPath("$.errors[0].field").isEqualTo("password")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(RegistrationRequestDTO.class, "password", NotBlank.class));
            }
            @Test
            void mismatchPatternPassword() {
                RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                        .name("Example")
                        .email("email@example.com")
                        .password("12")
                        .passwordConfirm("12")
                        .build();
                webClient.post()
                        .uri("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[*].code").value(hasItem("Pattern"))
                        .jsonPath("$.errors[*].field").value(hasItem("password"))
                        .jsonPath("$.errors[*].defaultMessage").value(hasItem(ValidationMessageExtractor.extractMessage(RegistrationRequestDTO.class, "password", Pattern.class)));
            }

            @Test
            void unmatchedPassword() {
                RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                        .name("Example")
                        .email("email@example.com")
                        .password("password")
                        .passwordConfirm("password1")
                        .build();
                webClient.post()
                        .uri("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[*].field").value(hasItem("passwordConfirm"))
                        .jsonPath("$.errors[*].defaultMessage").value(hasItem("Mật khẩu xác nhận không khớp"));
            }
            @Test
            void emailExists() {
                RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                        .name("Example")
                        .email("email@example.com")
                        .password("password")
                        .passwordConfirm("password")
                        .build();
                var ex =new UniqueKeyViolationException(
                        "Email đã được sử dụng",
                        "email",
                        request.getClass().getName(),
                        request.getEmail()
                );
                Mockito.when(authenticationService.register(request))
                        .thenReturn(Mono.error(ex));
                webClient.post()
                        .uri("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].field").isEqualTo("email")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ex.getMessage())
                        .jsonPath("$.errors[0].rejectedValue").isEqualTo(request.getEmail());
            }
        }
    }

    @Nested
    class Authenticate {
        @Test
        void happyPath() {
            var body = AuthenticationRequestDTO.builder().email("email@example").password("123").build();
            var response = AuthenticationResponseDTO.builder().accessToken("accessToken").refreshToken("refreshToken").build();
            Mockito.when(authenticationService.authenticate(any())).thenReturn(Mono.just(response));
            webClient.post()
                    .uri("/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.accessToken").exists()
                    .jsonPath("$.refreshToken").exists();
            Mockito.verify(authenticationService, Mockito.times(1)).authenticate(any());
        }
        @Nested
        class Validation{
            @Test
            void nullEmail() {
                AuthenticationRequestDTO request = AuthenticationRequestDTO.builder()
                        .email(null)
                        .password("password")
                        .build();
                webClient.post()
                        .uri("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("NotNull")
                        .jsonPath("$.errors[0].field").isEqualTo("email")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(AuthenticationRequestDTO.class, "email", NotNull.class));
            }

            @Test
            void invalidEmail() {
                AuthenticationRequestDTO request = AuthenticationRequestDTO.builder()
                        .email("email")
                        .password("password")
                        .build();
                webClient.post()
                        .uri("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("Email")
                        .jsonPath("$.errors[0].field").isEqualTo("email")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(AuthenticationRequestDTO.class, "email", Email.class));
            }
            @Test
            void nullPassword() {
                AuthenticationRequestDTO request = AuthenticationRequestDTO.builder()
                        .email("email@example")
                        .password(null)
                        .build();
                webClient.post()
                        .uri("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("NotNull")
                        .jsonPath("$.errors[0].field").isEqualTo("password")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(AuthenticationRequestDTO.class, "password", NotNull.class));
            }
            @Test
            void blankPassword() {
                AuthenticationRequestDTO request = AuthenticationRequestDTO.builder()
                        .email("email@example")
                        .password("     ")
                        .build();
                webClient.post()
                        .uri("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.errors[0].code").isEqualTo("NotBlank")
                        .jsonPath("$.errors[0].field").isEqualTo("password")
                        .jsonPath("$.errors[0].defaultMessage").isEqualTo(ValidationMessageExtractor.extractMessage(AuthenticationRequestDTO.class, "password", NotBlank.class));
            }
        }
    }
    @Test
    void authenticationFail() {
        var body = AuthenticationRequestDTO.builder().email("email@example").password("123").build();
        var ex  = new BadCredentialsException("Bad credentials");
        Mockito.when(authenticationService.authenticate(any())).thenReturn(Mono.error(ex));
        webClient.post()
                .uri("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Nested
    class RefreshToken {
        @Test
        void happyPath() {
            var response = AuthenticationResponseDTO.builder().accessToken("accessToken").refreshToken("refreshToken").build();
            Mockito.when(authenticationService.refreshToken(any())).thenReturn(Mono.just(response));
            webClient.post()
                    .uri("/refresh-token")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.accessToken").exists()
                    .jsonPath("$.refreshToken").exists();
            Mockito.verify(authenticationService, Mockito.times(1)).refreshToken(any());
        }
        @Test
        void malformedToken() {
            var ex  = new BadCredentialsException("Bad credentials");
            Mockito.when(authenticationService.refreshToken(any())).thenReturn(Mono.error(ex));
            webClient.post()
                    .uri("/refresh-token")
                    .exchange()
                    .expectStatus().isUnauthorized();
        }
    }
}