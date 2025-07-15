package com.prom.project.todolist.unit.service;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.RSAKey;
import com.prom.project.todolist.dto.TokenResponse;
import com.prom.project.todolist.service.impl.JwtGeneratorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtGeneratorTest {

    @Test
    void generateJwt_shouldCreateValidToken() throws Exception {
        final UserDetails user = mock(UserDetails.class);
        when(user.getUsername()).thenReturn("test-user");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_USER"))).when(user).getAuthorities();

        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        final KeyPair kp = kpg.generateKeyPair();

        final RSAKey realRsaJwk = new RSAKey.Builder((RSAPublicKey) kp.getPublic())
                .privateKey((RSAPrivateKey) kp.getPrivate())
                .keyID("test")
                .build();

        final JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();

        final JwtGeneratorImpl jwtGenerator = new JwtGeneratorImpl(realRsaJwk, header);
        ReflectionTestUtils.setField(jwtGenerator, "expiration", 30L);

        final Instant now = Instant.parse("2025-07-15T10:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class, CALLS_REAL_METHODS)) {
            mocked.when(Instant::now).thenReturn(now);

            final TokenResponse result = jwtGenerator.generateJwt(user); //result

            assertAll(
                    () -> assertNotNull(result.getToken()),
                    () -> assertEquals(Date.from(now), result.getIssued()),
                    () -> assertEquals(Date.from(now.plus(30, ChronoUnit.MINUTES)),
                            result.getExpires())
            );
        }
    }
}