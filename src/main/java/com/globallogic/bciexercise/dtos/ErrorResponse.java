package com.globallogic.bciexercise.dtos;

import com.globallogic.bciexercise.errors.ApiError;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    List<ApiError> error;

    public ErrorResponse() {
        this.error = new ArrayList<>();
    }

    public List<ApiError> getError() {
        return error;
    }

    public void setError(List<ApiError> error) {
        this.error = error;
    }
}
