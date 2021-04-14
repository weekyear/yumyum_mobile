package com.yumyum.domain.feed.dto;

import com.yumyum.domain.feed.entity.Feed;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TitleResponse {
    private String title;
    private String videoPath;

    public TitleResponse(Feed feed){
        this.title = feed.getTitle();
        this.videoPath = feed.getVideoPath();
    }
}
