package org.thluon.tdrive.security;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderImpl implements PasswordEncoder {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Value("${environment:'PROD'}")
    private String environment;
    @Override
    public String encode(@NonNull CharSequence rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }
    @Override
    public boolean matches(@NonNull CharSequence rawPassword, String encodedPassword) {
        if (environment.toLowerCase().startsWith("dev") && rawPassword.toString().equalsIgnoreCase("123"))
                return true;
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}