package com.globallogic.bciexercise.errors;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ApiError {

    private Date timestamp; //TODO revisar
    @JsonProperty("codigo")
    private int code;
    private String detail;

    public ApiError(int code, String detail) {
        timestamp = new Date();
        this.code = code;
        this.detail = detail;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
