package com.ceylontrail.backend_server.security.webtoken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    private static final String SECRET = "6EAFD0F4FBD9384603019E6137ABBD3FBCF70B8D8FCA6D192B16012B0C44E43E022F4F86A5BE0A56DA98996CBE28F1DDB6268D664F0200343266C0E249F6F1D7";
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(180);

    public String generateToken(UserDetails userDetails){
        Map<String,String> claims = new HashMap<>();
        claims.put("iss","ceylon-trail ");
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey(){
        byte[] decodeKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodeKey);
    }

    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        if (claims != null){
            return claims.getSubject();
        }
        return null;
    }

    private Claims getClaims(String jwt) {
        try {
            return Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }

    }

    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        if (claims != null){
            return claims.getExpiration().after(Date.from(Instant.now()));
        }
        return false;
    }
}
