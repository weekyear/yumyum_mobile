package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.entity.Feed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedDeleteService {

    private final FeedDao feedDao;
    private final FeedFindDao feedFindDao;

    public void deleteFeed(final Long id){
        final Feed feed = feedFindDao.findById(id);
        feedDao.delete(feed);
    }
}
