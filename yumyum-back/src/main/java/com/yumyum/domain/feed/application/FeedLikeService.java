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

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedLikeService {

    private final UserDao userDao;
    private final FeedDao feedDao;
    private final LikeDao likeDao;

    public Object likeFeed(final Long id, final LikeFeedRequest dto){
        final String userEmail = dto.getEmail();
        final List<Like> like = likeDao.findByUserEmailAndFeedId(userEmail, id);

        final boolean isCurLike = like.size() > 0;
        final Feed feed = feedDao.findById(id).get();
        final User user = userDao.findByEmail(userEmail).get();

        Long likeCount = feed.getLikeCount();
        if (!isCurLike) {
            Like newLike = new Like(user, feed);
            likeDao.save(newLike);
            likeCount += 1;
        } else {
            likeDao.delete(like.get(0));
            likeCount -= 1;
        }

        feed.likeFeed(likeCount, !isCurLike);

        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(feed), "success", HttpStatus.OK);
    }
}
