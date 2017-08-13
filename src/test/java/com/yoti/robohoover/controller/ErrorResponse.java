package com.yoti.robohoover.controller;

public class ErrorResponse {
    private int code;
    private String error;
    private String info;


    ErrorResponse(int code, String error, String info) {
        this.code = code;
        this.error = error;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public String getInfo() {
        return info;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code=" + code +
                ", error='" + error + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
