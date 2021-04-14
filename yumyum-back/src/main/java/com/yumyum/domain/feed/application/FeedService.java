package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final LikeDao likeDao;

    public boolean isLikeFeedOfUser(String userEmail, Feed feed) {
        final List<Like> list = likeDao.findByUserEmailAndFeedId(userEmail, feed.getId());
        return !list.isEmpty();
    }
}