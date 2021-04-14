package com.yumyum.domain.user.dto;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    String userEmail;
    String password;
    String newPassword;
}
