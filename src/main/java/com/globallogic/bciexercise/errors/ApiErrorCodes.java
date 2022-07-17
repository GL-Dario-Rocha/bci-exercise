package com.globallogic.bciexercise.errors;

public enum ApiErrorCodes {

    INVALID_EMAIL(1),
    INVALID_PASSWORD(2),
    ALREADY_EXISTING_RESOURCE(3),
    ENCRYPTION_ERROR(4),
    TOKEN_ERROR(5),
    USER_NOT_FOUND(6),
    DECRYPTION_ERROR(7),
    INTERNAL_SERVER_EXCEPTION(999);

    private final int code;

    ApiErrorCodes(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
