package com.zhaofei.framework.user.api.exception;

public class UserException extends RuntimeException {

    private String code;

    private String message;


    public UserException() {
        super();
    }

    public UserException(String code){
        this.code = code;
    }

    public UserException(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
