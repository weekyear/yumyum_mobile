package com.yumyum.domain.feed.api;

import com.yumyum.domain.feed.application.*;
import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.dto.CreateFeedRequest;
import com.yumyum.domain.feed.dto.LikeFeedRequest;
import com.yumyum.domain.feed.dto.UpdateFeedRequest;
import com.yumyum.global.common.response.HttpUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedFindDao feedFindDao;
    private final FileService fileService;
    private final FeedCreateService feedCreateService;
    private final FeedUpdateService feedUpdateService;
    private final FeedDeleteService feedDeleteService;
    private final FeedLikeService feedLikeService;
    private final FeedTitleSearchService feedTitleSearchService;

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    @ApiOperation(value = "피드 등록")
    public Object create(
            @ModelAttribute @ApiParam(value = "게시글 등록 시 필요한 정보 (음식명 , 날짜 , 식당이름, 장소 , 점수 , 내용)", required = true) final CreateFeedRequest dto,
            @RequestParam("file") @Valid @NotNull @NotEmpty final MultipartFile mFile) {
        return feedCreateService.createFeed(dto, mFile);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @PostMapping(value = "/video", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "동영상 등록")
    public Object uploadVideo(@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        String url = fileService.upload(multipartFile);
        return HttpUtils.makeResponse("200", url, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @PutMapping
    @ApiOperation(value = "피드 수정")
    public Object update(@Valid @RequestBody @ApiParam(value = "게시글 정보 수정", required = true) final UpdateFeedRequest dto) {
        return feedUpdateService.updateFeed(dto);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    @ApiOperation(value = "단일 피드 조회")
    public Object searchId(@Valid @ApiParam(value = "id 값으로 검색", required = true) @PathVariable final Long feedId,
                           @Valid @ApiParam(value = "로그인 회원 id", required = true) @PathVariable final Long userId) {
        return feedFindDao.findById(feedId, userId);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/list/{userId}")
    @ApiOperation(value = "모든 유저의 피드 리스트 조회")
    public Object feedList(@Valid @ApiParam(value = "회원 id", required = true) @PathVariable final Long userId) {
        return feedFindDao.findAll(userId);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/user/list/{userId}")
    @ApiOperation(value = "한 유저의 피드 리스트 조회")
    public Object userFeedList(@Valid @ApiParam(value = "id 값으로 검색 ", required = true) @PathVariable final Long userId) {
        return feedFindDao.findByUserId(userId);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/titles/{email}")
    @ApiOperation(value = "한 유저의 피드 타이틀 리스트 조회")
    public Object titleList(
            @Valid @RequestBody @ApiParam(value = "title 별로 전체 조회", required = true) @PathVariable final Long userId) {
        return feedTitleSearchService.findByUserId(userId);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/list/{email}/{title}/")
    @ApiOperation(value = "한 유저의 하나의 title로 적힌 피드 리스트 조회")
    public Object titleList(@Valid @ApiParam(value = "title 별로 전체 조회", required = true) @PathVariable final String title,
                            @PathVariable final Long userId) {
        return feedTitleSearchService.findByUserIdAndTitle(userId, title);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "피드 삭제")
    public Object delete(@Valid @ApiParam(value = "id 값으로 피드 삭제", required = true) @PathVariable final Long id) {
        return feedDeleteService.deleteFeed(id);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @PutMapping("/like/{id}")
    @ApiOperation(value = "피드 좋아요")
    public Object update(@Valid @ApiParam(value = "feed_id 값으로 피드 좋아요 반영", required = true) @PathVariable final Long id,
                         @Valid @RequestBody @ApiParam(value = "게시글 정보 수정", required = true) final LikeFeedRequest dto) {
        return feedLikeService.likeFeed(id, dto);
    }
}