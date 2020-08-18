package com.web.blog.exception.account;

import com.web.blog.enums.ErrorCode;

public class CustomUserException extends RuntimeException {
    private ErrorCode errorCode;

    public CustomUserException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
    public CustomUserException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
