package com.globallogic.bciexercise.utils;

import com.globallogic.bciexercise.errors.ApiError;
import com.globallogic.bciexercise.errors.ApiErrorCodes;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {

    public static List<ApiError> isValidPassword(String password) {
        List<ApiError> errors = new ArrayList<>();
        if(password == null || password.trim().isEmpty()) {
            errors.add(new ApiError(ApiErrorCodes.INVALID_PASSWORD.getCode(), "password can not be empty"));
            return errors;
        }
        //Must contain exactly 1 Upper case letter
        boolean upperValidation = password.matches("^[^A-Z]*[A-Z][^A-Z]*$");
        if (!upperValidation) {
            errors.add(new ApiError(ApiErrorCodes.INVALID_PASSWORD.getCode(), "password must contain exactly 1 upper case letter"));
        }

        //Must contain exactly 2 numbers
        boolean numberValidation = password.matches("^[^0-9]*[0-9][^0-9]*[0-9][^0-9]*$");
        if(!numberValidation) {
            errors.add(new ApiError(ApiErrorCodes.INVALID_PASSWORD.getCode(), "password must contain exactly 2 numbers"));
        }

        //Must contain lower case
        boolean lowerValidation = password.matches("^(?=.+[a-z])[a-zA-Z0-9]+$");
        if(!lowerValidation) {
            errors.add(new ApiError(ApiErrorCodes.INVALID_PASSWORD.getCode(), "password must contain lower case"));
        }

        //Must contain between 8 - 12 characters
        boolean lengthValidation = password.matches("^(?=.*[a-z])[a-zA-Z0-9]{8,12}$");
        if (!lengthValidation)  {
            errors.add(new ApiError(ApiErrorCodes.INVALID_PASSWORD.getCode(), "password must contain between 8 - 12 characters"));
        }
        return errors;
    }
}
