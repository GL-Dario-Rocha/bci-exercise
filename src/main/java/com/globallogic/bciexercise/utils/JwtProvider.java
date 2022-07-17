package com.globallogic.bciexercise.utils;

import com.globallogic.bciexercise.errors.ApiError;
import com.globallogic.bciexercise.errors.ApiErrorCodes;
import com.globallogic.bciexercise.errors.TokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider implements TokenProvider {

    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(String subject) {
        return Jwts.builder().setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000L))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getContentFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            ApiError error = new ApiError(ApiErrorCodes.TOKEN_ERROR.getCode(),
                    "Error getting email from token, message: " + e.getMessage());
            throw new TokenException(error);
        }
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        } catch (MalformedJwtException | UnsupportedJwtException | ExpiredJwtException
                | IllegalArgumentException | SignatureException e) {
            String errorMessage = String.format("token validation error, message: %s", e.getMessage());
            ApiError error = new ApiError(ApiErrorCodes.TOKEN_ERROR.getCode(), errorMessage);
            logger.error(errorMessage);
            throw new TokenException(error);
        }
    }
}
