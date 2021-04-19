package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.dto.UpdateFeedRequest;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.user.application.RegexChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedUpdateService {

    private final FeedFindDao feedFindDao;
    private final RegexChecker regexChecker;

    public void updateFeed(final UpdateFeedRequest dto){
        regexChecker.stringCheck("Content", dto.getContent());

        final Feed feed = feedFindDao.findById(dto.getId());
        feed.updateFeed(dto);
    }
}
