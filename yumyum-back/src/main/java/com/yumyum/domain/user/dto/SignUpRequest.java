package com.yumyum.domain.user.dto;

import com.yumyum.domain.user.entity.User;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Collections;

@Data
@Valid
@ToString
public class SignUpRequest {

    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$")
    private String password;

    private String nickname;

    public User toEntity(final String encodePassword, final LocalDateTime nowTime){
        return User.builder()
                .email(email)
                .password(encodePassword)
                .nickname(nickname)
                .introduction("한줄로 자신을 소개해주세요.")
                .createdDate(nowTime)
                .modifiedDate(nowTime)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}