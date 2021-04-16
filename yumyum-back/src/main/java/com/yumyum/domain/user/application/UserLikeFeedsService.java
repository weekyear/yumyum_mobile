package com.yumyum.domain.user.application;

import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLikeFeedsService {

    private final LikeDao likeDao;

    public Object getLikeFeeds(final String email){
        List<Like> likes = likeDao.findAllByUserEmail(email);
        List<Feed> feeds = new ArrayList<>();

        for (Like like : likes) {
            Feed curFeed = like.getFeed();
            curFeed.setIsLikeUser(true);
            feeds.add(curFeed);
        }

        return HttpUtils.makeResponse("200", feeds, "success", HttpStatus.OK);
    }
}
