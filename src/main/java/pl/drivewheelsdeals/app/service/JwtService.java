package pl.drivewheelsdeals.app.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {
    private static final String SECRET = "c3VwZXItc2VjcmV0LXNlY3JldC1zZWNyZXQtc2VjcmV0LXNlY3JldC1zZWNyZXQtc2VjcmV0LXNlY3JldC1zZWNyZXQtc2VjcmV0LXNlY3JldC1zZWNyZXQtc2VjcmV0LXNlY3JldC1zZWNyZXQtc2VjcmV0LXNlY3JldC1zZWNyZXQtc2VjcmV0LXNlY3JldC1rZXk"; // Todo load from application.properties
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public String generateToken(String username) {

        return Jwts.builder().subject(username).signWith(getSigningKey()).compact();
    }

    public String extractUsername(String token) throws BadRequestException {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            throw new BadRequestException();
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
