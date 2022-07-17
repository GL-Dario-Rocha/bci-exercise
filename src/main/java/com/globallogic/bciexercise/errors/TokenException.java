package com.globallogic.bciexercise.errors;

public class TokenException extends RuntimeException {

    private final ApiError error;

    public TokenException(ApiError error) {
        super();
        this.error = error;
    }

    public ApiError getError() {
        return error;
    }
}
