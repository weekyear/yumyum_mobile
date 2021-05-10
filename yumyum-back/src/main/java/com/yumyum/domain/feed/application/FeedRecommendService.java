package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.application.recommend.CollaborativeFiltering;
import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.dto.FeedResponse;
import com.yumyum.domain.user.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedRecommendService {

    private final UserDao userDao;
    private final FeedDao feedDao;
    private final FeedFindDao feedFindDao;
    private final LikeDao likeDao;
    private final FeedResponseService feedResponseService;

    private final CollaborativeFiltering collaborativeFiltering;

    public List<FeedResponse> getRecommendList(final Long userId){
        // 협업 필터링 추천순
        final List<FeedResponse> aList = collaborativeFiltering.getRecommendList(userId);
        if(aList.size() > 0) return aList;

        // 좋아요 인기순

        // 최신순
        return aList;
    }


}
