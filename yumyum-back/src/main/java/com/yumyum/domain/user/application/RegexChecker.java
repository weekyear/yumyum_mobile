package com.yumyum.domain.user.application;

import com.yumyum.global.error.exception.InvalidParameterException;
import org.springframework.stereotype.Service;

@Service
public class RegexChecker {

    // 이메일 형식이 맞는지 확인
    public void emailCheck(String email) {

        String EMAIL_REGEX = "^[_a-z0-9-]+(._a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        if(email == null || !email.matches(EMAIL_REGEX)){ // 유효한 값이 아니라면
            throw new InvalidParameterException("Email is Invalid");
        }
    }

    // 전화번호 형식이 맞는지 확인
    public void phoneCheck(String phoneNum){

        String regExp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
        if(phoneNum == null || !phoneNum.matches(regExp)){ // 유효한 값이 아니라면
            throw new InvalidParameterException("Phone Number is Invalid");
        }
    }

    // String 값이 유효한 값인지 확인 (Null이거나 ""이 아니어야 함)
    public void stringCheck(String type, String str){

        if(str == null || str.equals("")) { // 유효한 값이 아니라면
            throw new InvalidParameterException(type + " is Invalid");
        }
    }
}
