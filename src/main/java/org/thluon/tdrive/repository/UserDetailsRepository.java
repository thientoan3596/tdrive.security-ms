package org.thluon.tdrive.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.thluon.tdrive.entity.UserDetailsImpl;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserDetailsRepository extends R2dbcRepository<UserDetailsImpl, UUID> {
    Mono<UserDetails> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);
}
