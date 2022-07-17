package com.globallogic.bciexercise.errors;

public class UserNotFoundException extends RuntimeException {

    private final ApiError error;

    public UserNotFoundException(ApiError error) {
        super();
        this.error = error;
    }

    public ApiError getError() {
        return error;
    }
}
