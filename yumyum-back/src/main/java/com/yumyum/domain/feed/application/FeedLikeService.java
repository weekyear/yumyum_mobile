package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.dto.LikeFeedRequest;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedLikeService {

    private final UserDao userDao;
    private final FeedDao feedDao;
    private final LikeDao likeDao;

    public Object doLikeFeed(final LikeFeedRequest dto){
        final Optional<Feed> feed = feedDao.findById(dto.getFeedId());
        if(!feed.isPresent()){
            return HttpUtils.makeResponse("404", null, "feed not found", HttpStatus.NOT_FOUND);
        }

        final Optional<User> user = userDao.findById(dto.getUserId());
        if(!user.isPresent()){
            return HttpUtils.makeResponse("404", null, "user not found", HttpStatus.NOT_FOUND);
        }

        likeDao.save(dto.toEntity(feed.get(), user.get()));
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    public Object doCancelLikeFeed(final Long feedId, final Long userId){
        final Optional<Like> like = likeDao.findByFeedIdAndUserId(feedId, userId);
        if(!like.isPresent()){
            return HttpUtils.makeResponse("404", null, "like not found", HttpStatus.NOT_FOUND);
        }

        likeDao.delete(like.get());
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }
}
