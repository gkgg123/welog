package com.web.blog.exception.account;

import com.web.blog.enums.ErrorCode;

public class DuplicatedEmailException extends BusinessException{
  public DuplicatedEmailException(String msg){
    super(msg, ErrorCode.EMAIL_DUPLICATION);
  }
}
