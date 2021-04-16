package com.yumyum.domain.map.api;

import com.yumyum.domain.map.application.PlaceSearchService;
import com.yumyum.domain.map.application.PlaceService;
import com.yumyum.domain.map.dao.PlaceFindDao;
import com.yumyum.domain.map.dto.PlaceRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceFindDao placeFindDao;
    private final PlaceService placeService;
    private final PlaceSearchService placeSearchService;

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "맛집 등록", notes = "주소, 전화번호, 장소명, 좌표값을 받아 맛집을 등록한다.")
    @PostMapping("")
    public Object createPlace(@RequestBody final PlaceRequest dto) {
        return placeService.createPlace(dto);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "맛집 목록 조회", notes = "모든 맛집 목록을 불러온다.")
    @GetMapping("/list")
    public Object placeList() {
        return placeFindDao.findAll();
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "맛집 목록 검색", notes = "주소 혹은 장소명으로 맛집 목록을 검색한다.")
    @GetMapping("/list/{type}/{keyword}")
    public Object placeListByKeyword(@PathVariable final String type, @PathVariable final String keyword) {
        return placeSearchService.findByKeyword(type, keyword);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "맛집 조회", notes = "맛집 번호로 맛집 정보를 조회한다.")
    @GetMapping("/{placeId}")
    public Object placeInfo(@PathVariable final Long placeId) {
        return placeFindDao.findById(placeId);
    }
}