package com.yumyum.domain.user.dto;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRequest {

    private String email;

    private String introduction;

    private String nickname;

    private String profilePath;
}
