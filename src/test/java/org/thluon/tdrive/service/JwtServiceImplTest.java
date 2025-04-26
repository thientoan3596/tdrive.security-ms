package org.thluon.tdrive.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.thluon.tdrive.entity.UserDetailsImpl;

import java.util.UUID;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtServiceImplTest {
    private JwtServiceImpl underTest;

    private UserDetails userDetails;
    @BeforeAll
    void init() {
        underTest = new JwtServiceImpl();
        ReflectionTestUtils.setField(underTest, "jwtSecret", "feTxCwlxPHtSzxqSmRJQmQGCbA/gw3feFhFJ9/2xxSk=");
        ReflectionTestUtils.setField(underTest, "expiration", 36_000_000L);
        ReflectionTestUtils.setField(underTest, "refreshTokenExpiration", 604_800_000L);
        underTest.init();
        userDetails = UserDetailsImpl.builder()
                .id(UUID.randomUUID())
                .hashedPassword("hashedPassword")
                .email("example@example")
                .role("ROLE_USER")
                .name("John Doe")
                .build();
    }

    @Nested
    class GenerateToken {
        @Test
        void generateToken_shouldReturnToken() {
            String token = underTest.generateToken(userDetails);
            Assertions.assertNotNull(token);
        }
    }

}