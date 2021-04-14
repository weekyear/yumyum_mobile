package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.entity.User;
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
public class FeedFindDao {

    private final FeedDao feedDao;
    private final LikeDao likeDao;
    private final UserDao userDao;

    public Object findById(final Long feedId){
        final Optional<Feed> feed = feedDao.findById(feedId);

        if (!feed.isPresent()) {
            return HttpUtils.makeResponse("404", null, "No searchResult", HttpStatus.NOT_FOUND);
        }

        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(feed.get()), "success", HttpStatus.OK);
    }

    public Object findById(final Long feedId, final Long userId){
        final Optional<Feed> feed = feedDao.findById(feedId);

        if (!feed.isPresent()) {
            return HttpUtils.makeResponse("404", null, "No searchResult", HttpStatus.NOT_FOUND);
        }

        boolean isLikeFeedOfUser = isLikeFeedOfUser(feedId, userId);
        feed.get().setIsLikeUser(isLikeFeedOfUser);

        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(feed.get()), "success", HttpStatus.OK);
    }

    public Object findAll(final Long userId){
        final List<Feed> list = feedDao.findAll();
        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(list), "success" + list.size(), HttpStatus.OK);
    }

    public Object findByUserId(final Long userId){
        final Optional<User> user = userDao.findById(userId);

        if (!user.isPresent()) {
            return HttpUtils.makeResponse("404", null, "User Not Found", HttpStatus.NOT_FOUND);
        }

        List<Feed> list = feedDao.findAllByUserOrderByIdDesc(user.get());
        for (Feed feed : list) {
            feed.setIsLikeUser(isLikeFeedOfUser(feed.getId(), userId));
        }
        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(list), "success" + list.size(), HttpStatus.OK);
    }

    public boolean isLikeFeedOfUser(final Long feedId, final Long userId) {
        final List<Like> list = likeDao.findByFeedIdAndUserId(feedId, userId);
        return !list.isEmpty();
    }
}
