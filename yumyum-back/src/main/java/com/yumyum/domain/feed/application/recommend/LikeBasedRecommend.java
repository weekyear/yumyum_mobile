package com.yumyum.domain.feed.application.recommend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeBasedRecommend {

//    private final LikeFindDao likeFindDao;
//    private final FeedSearchService feedSearchService;
//
//    public List<FeedResponse> getRecommendList(final Long userId){
//        final List<FeedResponse> fList = feedSearchService.findAllDesc(userId);
//        final List<FeedResponse> list = new ArrayList<>();
//        for(FeedResponse feed : fList){
//            User user = feed.getUser();
//            if(likeFindDao.)
//        }
//        return list;
//    }
}
