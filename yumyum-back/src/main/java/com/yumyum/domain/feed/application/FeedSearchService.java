package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.dto.FeedResponse;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedSearchService {

    private final FeedDao feedDao;
    private final FeedFindDao feedFindDao;
    private final LikeDao likeDao;
    private final UserFindDao userFindDao;

    public List<FeedResponse> findAll(final Long userId){
        final List<FeedResponse> list = new ArrayList<>();
        final List<Feed> fList = feedDao.findAll();
        for(Feed feed : fList){
            list.add(new FeedResponse(feed, getLikeCount(feed.getId()), checkIsLike(feed.getId(), userId)));
        }
        return list;
    }

    public List<FeedResponse> findByAuthor(final Long authorId, final Long userId){
        final User user = userFindDao.findById(authorId);
        final List<FeedResponse> list = new ArrayList<>();
        final List<Feed> fList = feedDao.findByUser(user);
        for(Feed feed : fList){
            list.add(new FeedResponse(feed, getLikeCount(feed.getId()), checkIsLike(feed.getId(), userId)));
        }
        return list;
    }

    public List<FeedResponse> findByLike(final Long userId){
        final List<Like> lList = likeDao.findByUserId(userId);
        final List<FeedResponse> list = new ArrayList<>();
        for(Like like : lList){
            Feed feed = like.getFeed();
            list.add(new FeedResponse(feed, getLikeCount(feed.getId()), true));
        }
        return list;
    }

    public FeedResponse findById(final Long feedId, final Long userId){
        final Feed feed = feedFindDao.findById(feedId);
        final FeedResponse response = new FeedResponse(feed, getLikeCount(feedId), checkIsLike(feedId, userId));
        return response;
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
