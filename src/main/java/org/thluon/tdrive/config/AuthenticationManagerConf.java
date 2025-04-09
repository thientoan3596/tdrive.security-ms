package org.thluon.tdrive.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConf {
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(
            ReactiveUserDetailsService reactiveUserDetailsService
    ) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
    }
}
