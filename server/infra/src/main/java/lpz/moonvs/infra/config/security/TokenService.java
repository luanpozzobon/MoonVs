package lpz.moonvs.infra.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lpz.moonvs.domain.auth.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    private Instant expiration;

    private String generateToken(final User user) {
        try {
            final Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("moonvs")
                    .withSubject(user.getId().getValue())
                    .withExpiresAt(this.expiration)
                    .sign(algorithm);
        } catch (final JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar o token: ", e);
        }
    }

    public ResponseCookie generateCookieToken(final User user) {
        this.expiration = generateExpirationDate();
        final Instant now = Instant.now(Clock.system(ZoneId.of("-03:00")));
        final String token = this.generateToken(user);

        return ResponseCookie.from("token", token)
                .httpOnly(Boolean.TRUE)
                .path("/")
                .maxAge(Duration.between(now, expiration).toSeconds())
                .sameSite("Lax")
                .secure(Boolean.FALSE)
                .build();
    }

    public String validateToken(final String token) {
        try {
            final Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("moonvs")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (final JWTVerificationException e) {
            return "";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
    }
}
