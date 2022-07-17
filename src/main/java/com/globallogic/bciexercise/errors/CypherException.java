package com.globallogic.bciexercise.errors;

public class CypherException extends RuntimeException {

    private final ApiError error;

    public CypherException(ApiError error) {
        super();
        this.error = error;
    }

    public ApiError getError() {
        return error;
    }
}
