package com.yumyum.domain.user.application;

import org.springframework.stereotype.Service;

@Service
public class RegexChecker {

    // 이메일 형식이 맞는지 확인
    public boolean emailCheck(String email) {

        if(email == null) return false;
        String EMAIL_REGEX = "^[_a-z0-9-]+(._a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Boolean b = email.matches(EMAIL_REGEX);
        return b;
    }

    // 전화번호 형식이 맞는지 확인
    public boolean phoneCheck(String phoneNum){

        String regExp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
        return phoneNum.matches(regExp);
    }

    // String 값이 유효한 값인지 확인 (Null이거나 ""이 아니어야 함)
    public boolean stringCheck(String str){

        if(str != null && !str.equals("")) return true;
        else return false;
    }
}
