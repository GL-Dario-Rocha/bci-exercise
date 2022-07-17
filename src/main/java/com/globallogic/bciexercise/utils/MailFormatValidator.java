package com.globallogic.bciexercise.utils;


import com.globallogic.bciexercise.errors.ApiError;
import com.globallogic.bciexercise.errors.ApiErrorCodes;

import java.util.ArrayList;
import java.util.List;

public class MailFormatValidator {

    public static List<ApiError> isValidMail(String email) {
        List<ApiError> errors = new ArrayList<>();

        if(email == null || email.trim().isEmpty()) {
            errors.add(new ApiError(ApiErrorCodes.INVALID_EMAIL.getCode(), "email can not be empty"));
            return errors;
        }

        boolean isValidFormat = email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        if(!isValidFormat) {
            errors.add(new ApiError(ApiErrorCodes.INVALID_EMAIL.getCode(), "Invalid mail format"));
        }
        return errors;
    }
}
