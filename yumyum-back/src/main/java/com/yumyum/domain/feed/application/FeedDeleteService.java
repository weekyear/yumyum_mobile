package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedDeleteService {

    private final FeedDao feedDao;
    private final LikeDao likeDao;

    public Object deleteFeed(final Long id){
        final Optional<Feed> feed = feedDao.findById(id);

        if (!feed.isPresent()) {
            return HttpUtils.makeResponse("404", null, "Feed Not Found", HttpStatus.NOT_FOUND);
        }

        final List<Like> likeList = likeDao.findAllById(id);
        for (Like like : likeList) {
            likeDao.delete(like);
        }
        feedDao.delete(feed.get());
        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(feed.get()), "success", HttpStatus.OK);
    }
}
