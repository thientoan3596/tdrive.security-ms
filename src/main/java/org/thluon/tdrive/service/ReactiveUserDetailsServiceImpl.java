package org.thluon.tdrive.service;

import com.github.thientoan3596.exception.UniqueKeyViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.thluon.tdrive.entity.UserDetailsImpl;
import org.thluon.tdrive.repository.UserDetailsRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {
    private final UserDetailsRepository userDetailsRepository;
    public Mono<UserDetails> insertUser(@NonNull UserDetailsImpl userDetails) throws UniqueKeyViolationException {
        return userDetailsRepository.existsByEmail(userDetails.getEmail())
                .flatMap(exists->{
                    if(exists)
                        return Mono.error(new UniqueKeyViolationException("Email đã được sử dụng", "email", userDetails.getClass().getName(), userDetails.getEmail()));
                    return userDetailsRepository.insert(userDetails);
                });
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userDetailsRepository.findByEmail(username).cast(UserDetails.class);
    }
}
