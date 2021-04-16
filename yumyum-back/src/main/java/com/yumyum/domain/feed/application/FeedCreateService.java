package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dto.CreateFeedRequest;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.map.dao.PlaceFindDao;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.application.RegexChecker;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedCreateService {

    private final FeedDao feedDao;
    private final UserFindDao userFindDao;
    private final PlaceFindDao placeFindDao;
    private final FileService fileService;
    private final RegexChecker regexChecker;

    public void createFeed(final CreateFeedRequest dto, final MultipartFile file){
        final User user = userFindDao.findById(dto.getUserId());
        final Place place = placeFindDao.findById(dto.getPlaceId());

        regexChecker.stringCheck("Title", dto.getTitle());
        regexChecker.stringCheck("Content", dto.getContent());

        final String videoPath = fileService.upload(file);
        final Feed feed = feedDao.save(dto.toEntity(user, place, videoPath));
        feedDao.save(feed);
    }
}
