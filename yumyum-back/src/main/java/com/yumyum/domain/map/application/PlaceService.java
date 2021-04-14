package com.yumyum.domain.map.application;

import com.yumyum.domain.map.dao.PlaceDao;
import com.yumyum.domain.map.dto.PlaceRequest;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceDao placeDao;

    public Object savePlace(final PlaceRequest dto) {
        final Optional<Place> place = placeDao.findById(dto.getId());
        if (place.isPresent()) {
            return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(place.get()), "this place already exists",
                    HttpStatus.OK);
        }

        Place savedPlace = placeDao.save(dto.toEntity());

        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(savedPlace), "success", HttpStatus.OK);
    }
}
