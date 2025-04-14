package org.thluon.tdrive.controller;

import com.github.thientoan3596.dto.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thluon.tdrive.dto.AuthenticationRequestDTO;
import org.thluon.tdrive.dto.AuthenticationResponseDTO;
import org.thluon.tdrive.dto.RegistrationRequestDTO;
import reactor.core.publisher.Mono;

@Tag(name = "Authentication API")
@ApiResponses(value = {@ApiResponse(responseCode = "500", description = "Internal Server Error")})
public interface AuthenticationAPI {
    //region POST /register - register
    @Operation(summary = "Đăng ký")
    //region @Body
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Thông tin đăng ký",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationRequestDTO.class))
    )
    //endregion @Body
    //region @Responses
    @ApiResponse(responseCode = "201", description = "JWT", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Payload không hợp lệ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    //endregion
    @PostMapping("/register")
    default Mono<ResponseEntity<AuthenticationResponseDTO>> register(@RequestBody Mono<@Valid RegistrationRequestDTO> requestMono) {
        throw new IllegalStateException("Not yet implemented");
    }

    //endregion POST /register - register
    //region POST /authenticate - authenticate
    @Operation(summary = "Đăng nhập")
    //#region @Body
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Credentials",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationRequestDTO.class)))
    //#endregion
    //region @Responses
    @ApiResponse(responseCode = "200", description = "JWT", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "Authentication fail ( Could be refresh token expired, see message for more details )",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "423", description = "Tài khoản bị khóa ,see lockReason", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    //endregion
    @PostMapping("/authenticate")
    default Mono<ResponseEntity<AuthenticationResponseDTO>> authenticate(@Valid @RequestBody AuthenticationRequestDTO authenticationRequest) {
        throw new IllegalStateException("Not yet implemented");
    }
    //endregion POST /authenticate - authenticate
    //region POST /refresh-token - Refresh token
    @Operation(summary = "Refresh JWT token")
    @Parameter(
            name = "Authorization",
            description = "Bearer token for authentication",
            in = ParameterIn.HEADER,
            required = true
    )
    //region @Responses
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "Authentication fail ( Could be refresh token expired, see message for more details )",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    //endregion @Responses
    @PostMapping("/refresh-token")
    default Mono<ResponseEntity<AuthenticationResponseDTO>> refreshToken(ServerHttpRequest request) {
        throw new IllegalStateException("Not yet implemented");
    }
    //endregion POST /refresh-token - Refresh token
}
