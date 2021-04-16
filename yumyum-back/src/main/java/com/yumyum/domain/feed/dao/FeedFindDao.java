package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.dto.FeedResponse;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedFindDao {

    private final FeedDao feedDao;
    private final LikeDao likeDao;
    private final UserDao userDao;

    public Object findAll(final Long userId){
        final List<FeedResponse> list = new ArrayList<>();
        final List<Feed> fList = feedDao.findAll();
        for(Feed feed : fList){
            list.add(new FeedResponse(feed, getLikeCount(feed.getId()), checkIsLike(feed.getId(), userId)));
        }
        return HttpUtils.makeResponse("200", list, "success", HttpStatus.OK);
    }

    public Object findByAuthor(final Long authorId, final Long userId){
        final Optional<User> user = userDao.findById(authorId);
        if (!user.isPresent()) {
            return HttpUtils.makeResponse("404", null, "user not found", HttpStatus.NOT_FOUND);
        }

        final List<FeedResponse> list = new ArrayList<>();
        final List<Feed> fList = feedDao.findByUser(user.get());
        for(Feed feed : fList){
            list.add(new FeedResponse(feed, getLikeCount(feed.getId()), checkIsLike(feed.getId(), userId)));
        }
        return HttpUtils.makeResponse("200", list, "success", HttpStatus.OK);
    }

    public Object findByLike(final Long userId){
        final List<Like> lList = likeDao.findByUserId(userId);
        final List<FeedResponse> list = new ArrayList<>();
        for(Like like : lList){
            Feed feed = like.getFeed();
            list.add(new FeedResponse(feed, getLikeCount(feed.getId()), true));
        }
        return HttpUtils.makeResponse("200", list, "success", HttpStatus.OK);
    }

    public Object findById(final Long feedId, final Long userId){
        final Optional<Feed> feed = feedDao.findById(feedId);
        if (!feed.isPresent()) {
            return HttpUtils.makeResponse("404", null, "feed not found", HttpStatus.NOT_FOUND);
        }

        final FeedResponse response = new FeedResponse(feed.get(), getLikeCount(feed.get().getId()), checkIsLike(feed.get().getId(), userId));
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    public Long getLikeCount(final Long feedId){
        final Long likeCount = likeDao.countById(feedId);
        return likeCount;
    }

    public boolean checkIsLike(final Long feedId, final Long userId) {
        final Optional<Like> like = likeDao.findByFeedIdAndUserId(feedId, userId);
        return like.isPresent();
    }
}
