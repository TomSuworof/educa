package com.dreamteam.eduuca.config;

import com.dreamteam.eduuca.entities.User;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Log4j2
@Component
public class JwtUtils {
    private final static String jwtSecret = "eduucaSecretKey"; // should be more complex
    private final static Integer jwtExpirationMs = 999999999;

    public String generateJwtToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error(JWTError.INVALID_SIGNATURE.getMessage(), e.getMessage());
        } catch (MalformedJwtException e) {
            log.error(JWTError.INVALID_TOKEN.getMessage(), e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error(JWTError.TOKEN_IS_EXPIRED.getMessage(), e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(JWTError.TOKEN_IS_UNSUPPORTED.getMessage(), e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(JWTError.TOKEN_IS_EMPTY.getMessage(), e.getMessage());
        }

        return false;
    }
}

@Getter
@AllArgsConstructor
enum JWTError {
    INVALID_SIGNATURE("Invalid JWT signature: {}"),
    INVALID_TOKEN("Invalid JWT token: {}"),
    TOKEN_IS_EXPIRED("JWT token is expired: {}"),
    TOKEN_IS_UNSUPPORTED("JWT token is unsupported: {}"),
    TOKEN_IS_EMPTY("JWT claims string is empty: {}");

    private final String message;
}
