package com.gundan.terragold.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey; // Use SecretKey for HMAC

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit; // Import for clarity in time units

@Service
public class JwtService {

    // IMPORTANT: Secret must be securely stored and long (at least 256 bits for HS256)
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    // Use TimeUnit for clearer time configuration (e.g., 1 hour default)
    @Value("${jwt.expiration.ms:3600000}")
    private long EXPIRATION_TIME;

    // --- Key Management ---
    // Use SecretKey for symmetric signing like HS256
    private SecretKey getSigningKey() {
        // Decode the Base64-encoded secret key string
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // Create an HMAC SHA key for signing
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // --- Token Generation ---
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // You can add user roles or other non-sensitive data to claims here

        return buildToken(claims, username, EXPIRATION_TIME);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String subject,
            long expirationTime
    ) {
        long nowMillis = System.currentTimeMillis();
        Date issuedAt = new Date(nowMillis);
        Date expiration = new Date(nowMillis + expirationTime);

        return Jwts.builder()
                // 1. Set Claims (Payload)
                // The modern API uses a .claims().add() or .claims().set() structure
                .claims()
                .add(extraClaims)
                .subject(subject) // Standard 'sub' claim (usually username/ID)
                .issuedAt(issuedAt) // Standard 'iat' claim
                .expiration(expiration) // Standard 'exp' claim
                .and() // Return to the JwtBuilder instance

                // 2. Sign the JWT (Header + Payload)
                // Use the modern signWith(SecretKey) for default algorithm (HS256)
                // or signWith(SecretKey, SecureDigestAlgorithm) if needed
                .signWith(getSigningKey())

                // 3. Compact (Serialization)
                .compact();
    }

    // Example Token Validation (Parser)
    public Claims extractAllClaims(String token) {
        // The modern parser uses Jwts.parser().verifyWith(key).build().parseClaimsJws(token)
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Use verifyWith() instead of setSigningKey()
                .build() // Build the immutable parser instance
                .parseSignedClaims(token)
                .getPayload();
    }
}