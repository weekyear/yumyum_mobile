package com.yumyum.domain.user.dto;

import com.yumyum.domain.user.entity.User;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    User user;
    String token;
}
