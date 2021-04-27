package com.yumyum.domain.map.application;

import com.yumyum.domain.map.dao.PlaceDao;
import com.yumyum.domain.map.dto.PlaceRequest;
import com.yumyum.domain.user.application.RegexChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceCreateService {

    private final PlaceDao placeDao;
    private final RegexChecker regexChecker;

    public void createPlace(final PlaceRequest dto) {
        regexChecker.stringCheck("Address", dto.getAddress());
        regexChecker.phoneCheck(dto.getPhone());
        regexChecker.stringCheck("Name", dto.getName());

        if(!placeDao.existsByAddressAndName(dto.getAddress(), dto.getName())){
            placeDao.save(dto.toEntity());
        }
    }
}
