package com.globallogic.bciexercise.errors;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<ApiError> errors;

    public ValidationException() {
        errors = new ArrayList<>();
    }

    public ValidationException(List<ApiError> errors) {
        super();
        this.errors = errors;
    }

    public List<ApiError> getErrors() {
        return errors;
    }
}
