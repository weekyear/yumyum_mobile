package com.yumyum.domain.user.exception;

import com.yumyum.global.error.exception.BusinessException;
import com.yumyum.global.error.exception.ErrorCode;

public class PasswordWrongException extends BusinessException {

    public PasswordWrongException() {
        super("Password is Wrong", ErrorCode.BAD_REQUEST);
    }
}
