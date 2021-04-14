package com.yumyum.domain.user.dto;

import com.yumyum.domain.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Collections;

@Data
@Valid
@ToString
public class SignUpRequest {
    @ApiModelProperty(required = true)
    @NotNull
    String email;
    @ApiModelProperty(required = true)
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$")
    String password;

    @ApiModelProperty(required = true)
    @NotNull
    String nickname;

    public User toEntity(String encodePassword, LocalDateTime nowTime){
        return User.builder()
                .email(email)
                .password(encodePassword)
                .nickname(nickname)
                .createdDate(nowTime)
                .modifiedDate(nowTime)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}