package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dto.CreateFeedRequest;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.map.application.PlaceCreateService;
import com.yumyum.domain.map.dao.PlaceDao;
import com.yumyum.domain.map.dto.PlaceRequest;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.application.RegexChecker;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedCreateService {

    private final FeedDao feedDao;
    private final PlaceDao placeDao;
    private final UserFindDao userFindDao;
    private final FileUploadService fileUploadService;
    private final FileThumbnailService fileThumbnailService;
    private final PlaceCreateService placeCreateService;
    private final RegexChecker regexChecker;

    public void createFeed(final CreateFeedRequest dto, final MultipartFile file){
        final User user = userFindDao.findById(dto.getUserId());
        final PlaceRequest placeRequest = dto.getPlaceRequest();

        regexChecker.stringCheck("Title", dto.getTitle());
        regexChecker.stringCheck("Content", dto.getContent());

        placeCreateService.createPlace(placeRequest);

        final Optional<Place> place = placeDao.findByAddressAndName(placeRequest.getAddress(), placeRequest.getName());
        final String videoPath = fileUploadService.upload(file);
        final String thumbnailPath = fileThumbnailService.createThumbnail(videoPath);
        final Feed feed = feedDao.save(dto.toEntity(user, place.get(), videoPath, thumbnailPath));
        feedDao.save(feed);
    }
}
