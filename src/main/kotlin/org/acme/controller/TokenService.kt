package org.acme.controller

import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.jwt.Claims
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@ApplicationScoped
class TokenService {

    fun generateToken(): String {
        val now = Instant.now()
        return Jwt.claims()
            .subject(UUID.randomUUID().toString())
            .upn("johndoe@example.com")
            .groups(setOf("SOME ROLE"))
            .issuer("A Issuer")
            .issuedAt(now)
            .expiresIn(Duration.ofHours(1))
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .claim(Claims.email, "johndoe@example.com")
            .claim(Claims.given_name, "John")
            .claim(Claims.family_name, "Doe")
            .claim(Claims.full_name, "John Doe")
            .claim("some_custom_claim", "a value")
            .sign()
    }
}