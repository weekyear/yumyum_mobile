package com.yumyum.domain.feed.dto;

import com.yumyum.domain.map.dto.PlaceResponse;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateFeedRequest {

    private Long id;

    private String title;

    private String content;

    private Long score;

    private PlaceResponse placeResponse;

    private Boolean isCompleted;
}
