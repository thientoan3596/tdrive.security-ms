package org.thluon.tdrive.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.thluon.tdrive.entity.UserDetailsImpl;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class UserDetailsRepositoryImpl implements UserDetailsRepository{
    private final R2dbcEntityTemplate template;

    @Override
    public Mono<UserDetailsImpl> findByEmail(String email) {
        return template.selectOne(Query.query(Criteria.where("email").is(email)), UserDetailsImpl.class);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return template.exists(Query.query(Criteria.where("email").is(email)), UserDetailsImpl.class);
    }

    @Override
    public Mono<UserDetails> insert(UserDetailsImpl payload) {
        return template.insert(UserDetailsImpl.class)
                .using(payload)
                .then(Mono.defer(()->findByEmail(payload.getEmail())));
    }
}
