package org.thluon.tdrive.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO {
    @NotNull(message="Không được bỏ trống")
    @NotBlank(message="Không được bỏ trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotNull(message ="Không được bỏ trống" )
    @NotBlank(message = "Không được bỏ trống")
    private String password;
    public UsernamePasswordAuthenticationToken toUsernamePasswordAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
