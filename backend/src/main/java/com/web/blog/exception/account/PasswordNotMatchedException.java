package com.web.blog.exception.account;

import com.web.blog.enums.ErrorCode;

public class PasswordNotMatchedException extends RuntimeException {
    private ErrorCode errorCode;

    public PasswordNotMatchedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
