package com.yumyum.domain.map.api;

import com.yumyum.domain.map.application.PlaceService;
import com.yumyum.domain.map.dao.PlaceFindDao;
import com.yumyum.domain.map.dto.PlaceRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceFindDao placeFindDao;
    private final PlaceService placeService;

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @PostMapping
    @ApiOperation(value = "place 저장")
    public Object save(@RequestBody @ApiParam(value = "place에 저장할 정보", required = true) final PlaceRequest dto) {
        return placeService.savePlace(dto);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    @ApiOperation(value = "id로 place,feed 전체 불러오기")
    public Object searchIdPlaceFeed(@Valid @ApiParam(value = "id 값으로 검색 ", required = true) @PathVariable final Long id) {
        return placeFindDao.findById(id);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/list")
    @ApiOperation(value = "모든 place 반환 ")
    public Object placesList() {
        return placeFindDao.findAll();
    }
}