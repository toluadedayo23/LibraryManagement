package com.test.librarymanagement.config.security;


import com.test.librarymanagement.domain.data.Token;
import com.test.librarymanagement.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthJwtService {

    @Value("${librarymanagement.security.jwt.secret-key}")
    private String secretKey;
    @Value("${librarymanagement.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${librarymanagement.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final TokenRepository tokenRepository;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Token generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public Token generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails);
    }

    private Token buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        String accessToken = Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
        String refreshToken = Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();

        Token token = Token.builder()
                .username(userDetails.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(token);

        return token;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            tokenRepository.findByAccessToken(token).orElseThrow(NoSuchElementException::new);
            Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token);
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && isTokenNotExpired(token);

        }
        catch(ExpiredJwtException e){
            log.error(e.getMessage());
            throw e;
        }
        catch (UnsupportedJwtException e) {
            log.error(e.getMessage());
            throw new UnsupportedJwtException("Jwt format does not match the expected format expected by the application");
        } catch (JwtException e) {
            log.error("JWT Exception {}", e.getMessage());
            throw new JwtException("JWT cannot be parsed or validated as required");
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument {}", e.getMessage());
            throw new IllegalArgumentException("Jwt cannot be null or empty");
        }
    }

    private boolean isTokenNotExpired(String token) {
        if (!extractExpiration(token).before(new Date())) {
            return true;
        }

        throw new ExpiredJwtException(null, null, "Invalid JWT: JWT token has expired");
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
            return Jwts
                    .parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String validateRefreshToken(String token) {
        if (tokenRepository.findByRefreshToken(token).isEmpty()) {
            throw new AuthenticationException("Invalid refresh token") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }

        return extractUsername(token);
    }

    public void revokeToken(String token) {
        tokenRepository.deleteByAccessToken(token);
    }
}