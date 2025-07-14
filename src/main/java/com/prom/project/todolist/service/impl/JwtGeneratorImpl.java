package com.prom.project.todolist.service.impl;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.prom.project.todolist.dto.TokenResponse;
import com.prom.project.todolist.service.JwtGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtGeneratorImpl implements JwtGenerator {

    private final RSAKey rsaKey;
    private final JWSHeader jwsHeader;

    @Value("${system.expiration-time}")
    private long expiration;

    @SneakyThrows
    @Override
    public TokenResponse generateJwt(final UserDetails userDetails) {
        final Instant now = Instant.now();
        final JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(expiration, ChronoUnit.MINUTES)))
                .build();

        final SignedJWT jwt = new SignedJWT(jwsHeader, claims);
        final JWSSigner signer = new RSASSASigner(rsaKey.toPrivateKey());
        jwt.sign(signer);

        return new TokenResponse(jwt.serialize(), claims.getIssueTime(), claims.getExpirationTime());
    }
}
