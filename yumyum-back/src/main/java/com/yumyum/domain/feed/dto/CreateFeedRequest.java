package com.yumyum.domain.feed.dto;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.entity.User;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFeedRequest {
    private String title;

    private String content;

    private Long score;

    private String userEmail;

    private Long placeId;

    public Feed toEntity(final User user, final Place place, final String videoPath){
        return Feed.builder()
                .title(title)
                .score(score)
                .content(content)
                .user(user)
                .place(place)
                .videoPath(videoPath)
                .likeCount(0L)
                .build();
    }
}
