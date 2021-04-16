package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dto.UpdateFeedRequest;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedUpdateService {

    private final FeedDao feedDao;

    public Object updateFeed(final UpdateFeedRequest dto){
        final Optional<Feed> feed = feedDao.findById(dto.getId());

        if (!feed.isPresent()) {
            return HttpUtils.makeResponse("404", null, "Feed Not Found", HttpStatus.NOT_FOUND);
        }

        feed.get().updateFeed(dto);

        return HttpUtils.makeResponse("200", feed.get(), "success", HttpStatus.OK);
    }
}
