package org.thluon.tdrive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Thông tin đăng ký")
public class RegistrationRequestDTO {
    @NotBlank(message = "Không được bỏ trống")
    @Size(min = 5, max = 255, message = "Phải từ {min} đến {max} ký tự")
    @Schema(description = "Tên")
    String name;
    @NotNull(message = "Không được bỏ trống")
    @Email(message = "Không hợp lệ")
    @Schema(description = "Email (unique)")
    String email;
    @NotBlank(message = "Mât khẩu không được bỏ trống")
    // TODO: 2/18/2025 Comment the following block and use the afterwards
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.* ).{3,16}$", message = "Mật khẩu phải từ 3-16 ký tự, bao gồm: 1 chữ số, 1 ký tự thường, 1 ký tự in hoa." )
    @Pattern(regexp = ".{3,255}", message = "Mật khẩu phải từ 3-16 ký tự, bao gồm:" +
            "            1 chữ số, " +
            "            1 ký tự thường" +
            "            1 ký tự in hoa. ")
    @Schema(description = "Mật khẩu")
    String password;
    @NotBlank(message = "Xác nhận mât khẩu không được bỏ trống")
    @Pattern(regexp = ".{3,255}", message = "Mật khẩu phải từ 3-16 ký tự, bao gồm:" +
            "            1 chữ số, " +
            "            1 ký tự thường" +
            "            1 ký tự in hoa. ")
    @Schema(description = "Mật khẩu xác nhận")
    String passwordConfirm;
}
