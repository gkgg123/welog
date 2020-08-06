package com.web.blog.exception.account;

import com.web.blog.enums.ErrorCode;

public class DuplicatedNicknameException extends BusinessException {
  public DuplicatedNicknameException(String msg){
    super(msg, ErrorCode.NICKNAME_DUPLICATION);
  }
}
