package com.globallogic.bciexercise.utils;

public interface TokenProvider {

    String generateToken(String subject);
    String getContentFromToken(String token);
    void validateToken(String token);
}
