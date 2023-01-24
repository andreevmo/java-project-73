package hexlet.code.app.config.jwt;

import hexlet.code.app.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder().setSubject(userDetails.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 7776000))
                .signWith(key)
                .compact();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (JwtException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public String getUsernameFromJwtToken(String jwt) {
        return Jwts.parserBuilder().build().parseClaimsJws(jwt).getBody().getSubject();
    }
}
