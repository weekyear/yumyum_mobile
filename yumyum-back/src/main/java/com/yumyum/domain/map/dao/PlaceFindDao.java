package com.yumyum.domain.map.dao;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.map.entity.Place;
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
public class PlaceFindDao {

    private final PlaceDao placeDao;
    private final FeedDao feedDao;

    public Object findById(final Long id){
        final Optional<Place> place = placeDao.findById(id);
        if (!place.isPresent()) {
            return HttpUtils.makeResponse("404", null, "No searchResult", HttpStatus.NOT_FOUND);
        }
        List<Feed> searchList = feedDao.findByPlace(place.get());
        return HttpUtils.makeResponse("200", searchList, "success", HttpStatus.OK);
    }

    public Object findAll(){
        final List<Place> places = placeDao.findAll();
        return HttpUtils.makeResponse("200", places, "success", HttpStatus.OK);
    }
}
