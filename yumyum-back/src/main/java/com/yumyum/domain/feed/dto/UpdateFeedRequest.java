package com.yumyum.domain.feed.dto;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateFeedRequest {
    private Long id;
    private String content;
    private Long score;
}
