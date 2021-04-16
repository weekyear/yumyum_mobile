package com.yumyum.domain.map.application;

import com.yumyum.domain.map.dao.PlaceDao;
import com.yumyum.domain.map.dto.PlaceRequest;
import com.yumyum.domain.map.dto.PlaceResponse;
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

    public Object createPlace(final PlaceRequest dto) {
        final Optional<Place> place = placeDao.findByAddressAndName(dto.getAddress(), dto.getName());
        if (place.isPresent()) {
            return HttpUtils.makeResponse("200", place.get(), "this place already exists",
                    HttpStatus.OK);
        }

        Place savedPlace = placeDao.save(dto.toEntity());

        return HttpUtils.makeResponse("200", new PlaceResponse(savedPlace), "success", HttpStatus.OK);
    }
}
