package org.thluon.tdrive.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.thluon.tdrive.entity.UserDetailsImpl;
import reactor.core.publisher.Mono;

public interface UserDetailsRepository{
    Mono<UserDetailsImpl> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);
    Mono<UserDetails> insert(UserDetailsImpl payload);
}
