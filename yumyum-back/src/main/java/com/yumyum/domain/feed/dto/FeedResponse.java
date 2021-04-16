package com.yumyum.domain.feed.dto;

import com.yumyum.domain.feed.entity.Feed;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedResponse {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private Long placeId;

    private String videoPath;

    private String thumnailPath;

    private Long likeCount;

    private Boolean isLike;

    public FeedResponse(final Feed feed, final Long likeCount, final Boolean isLike){
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.userId = feed.getUser().getId();
        this.placeId = feed.getPlace().getId();
        this.videoPath = feed.getVideoPath();
        this.thumnailPath = feed.getThumbnailPath();
        this.likeCount = likeCount;
        this.isLike = isLike;
    }
}
