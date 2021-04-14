package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dto.CreateFeedRequest;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.map.dao.PlaceDao;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedCreateService {

    private final FeedDao feedDao;
    private final UserDao userDao;
    private final PlaceDao placeDao;
    private final FeedService feedService;
    private final FileService fileService;

    public Object createFeed(final CreateFeedRequest dto, final MultipartFile mFile){
        final String title = dto.getTitle().trim();
        final String content = dto.getContent().trim();
        final Long score = dto.getScore();
        final Optional<User> user = userDao.findByEmail(dto.getUserEmail().trim());
        final Optional<Place> place = placeDao.findById(dto.getPlaceId());

        if (!user.isPresent()) {
            return HttpUtils.makeResponse("400", null, "User Not found", HttpStatus.BAD_REQUEST);
        }

        if (!place.isPresent()) {
            return HttpUtils.makeResponse("400", null, "Place Not found", HttpStatus.BAD_REQUEST);
        }

        if ("".equals(title) || "".equals(place.get().getAddressName()) || "".equals(place.get().getPlaceName())
                || score == null || "".equals(content)) {
            return HttpUtils.makeResponse("400", null, "data is blank", HttpStatus.BAD_REQUEST);
        }

        final String videoPath = fileService.upload(mFile);

        final Feed feed = feedDao.save(dto.toEntity(user.get(), place.get(), videoPath));

        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(feed), "success", HttpStatus.OK);
    }
}
