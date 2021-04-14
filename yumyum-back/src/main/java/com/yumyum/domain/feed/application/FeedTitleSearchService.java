package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.dto.TitleResponse;
import com.yumyum.domain.feed.entity.Feed;
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
public class FeedTitleSearchService {

    private final FeedDao feedDao;
    private final UserDao userDao;
    private final FeedFindDao feedFindDao;

    public Object findByUserId(final Long userId){
        final Optional<User> user = userDao.findById(userId);

        if (!user.isPresent()) {
            return HttpUtils.makeResponse("404", null, "User Not Found", HttpStatus.NOT_FOUND);
        }

        final List<Feed> fList = feedDao.findByUserId(userId);
        List<TitleResponse> tList = new ArrayList<>();

        for (Feed feed : fList) {
            tList.add(new TitleResponse(feed));
        }

        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(tList), "success " + fList.size(), HttpStatus.OK);
    }

    public Object findByUserIdAndTitle(final Long userId, final String title){
        final Optional<User> user = userDao.findById(userId);

        if (!user.isPresent()) {
            return HttpUtils.makeResponse("404", null, "User Not Found", HttpStatus.NOT_FOUND);
        }

        List<Feed> feedList = feedDao.findAllByUserIdAndTitle(userId, title);
        for (Feed feed : feedList) {
            feed.setIsLikeUser(feedFindDao.isLikeFeedOfUser(feed.getId(), userId));
        }
        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(feedList), "success" + feedList.size(), HttpStatus.OK);
    }
}
