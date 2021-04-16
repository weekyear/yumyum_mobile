package com.yumyum.domain.map.application;

import com.yumyum.domain.map.dao.PlaceDao;
import com.yumyum.domain.map.dto.PlaceResponse;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceSearchService {

    private final PlaceDao placeDao;

    public Object findByKeyword(final String type, final String keyword){
        if(!type.equals("address") && !type.equals("name")){
            return HttpUtils.makeResponse("400", null, "type is invalid", HttpStatus.NOT_FOUND);
        }

        final List<PlaceResponse> list = entityToDto(type, keyword);
        return HttpUtils.makeResponse("200", list, "success", HttpStatus.OK);
    }

    public List<PlaceResponse> entityToDto(final String type, final String keyword){
        List<PlaceResponse> list = new ArrayList<>();

        switch (type){
            case "address":
                final List<Place> aList = placeDao.findByAddressContainingIgnoreCase(keyword);
                for(Place place : aList){
                    list.add(new PlaceResponse(place));
                }
                break;
            case "name":
                final List<Place> nList = placeDao.findByNameContainingIgnoreCase(keyword);
                for(Place place : nList){
                    list.add(new PlaceResponse(place));
                }
                break;
            default:
                break;
        }

        return list;
    }
}
