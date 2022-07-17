package com.globallogic.bciexercise.errors;

public class DuplicatedUserException extends RuntimeException {

    private final ApiError error;

    public DuplicatedUserException(ApiError error) {
        super();
        this.error = error;
    }

    public ApiError getError() {
        return error;
    }
}
