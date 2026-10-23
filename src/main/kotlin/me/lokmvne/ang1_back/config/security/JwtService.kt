package me.lokmvne.ang1_back.config.security

import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class JwtService(
    private val jwtEncoder: JwtEncoder
) {

    fun generateToken(
        userId: Long,
        email: String
    ): String {

        val now = Instant.now()

        val claims = JwtClaimsSet.builder()
            .issuer("ang1-back")
            .subject(userId.toString())
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .claim("email", email)
            .build()

        val headers = JwsHeader
            .with(MacAlgorithm.HS256)
            .build()

        return jwtEncoder.encode(
            JwtEncoderParameters.from(headers, claims)
        ).tokenValue
    }
}