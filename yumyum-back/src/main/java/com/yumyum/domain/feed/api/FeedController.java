package com.yumyum.domain.feed.api;

import com.yumyum.domain.feed.application.*;
import com.yumyum.domain.feed.dto.*;
import com.yumyum.global.common.response.HttpUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedCreateService feedCreateService;
    private final FeedUpdateService feedUpdateService;
    private final FeedDeleteService feedDeleteService;
    private final FeedSearchService feedSearchService;
    private final FeedLikeService feedLikeService;
    private final FileServiceTest fileServiceTest;

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "피드 등록", notes = "제목, 내용, 평점, 회원 번호, 장소 번호, 동영상으로 피드를 등록한다.")
    @PostMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public Object createFeed(@RequestBody final CreateFeedRequest dto, @RequestParam final MultipartFile file) {
        feedCreateService.createFeed(dto, file);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "동영상 및 썸네일 등록 테스트", notes = "동영상에서 썸네일을 추출하여 동영상과 함꼐 저장한다.")
    @PostMapping("/video/test")
    public Object uploadVideoTest(@RequestParam MultipartFile file) {
        FileDto response = fileServiceTest.uploadFeedMedia(file);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "피드 수정", notes = "피드 번호로 내용과 평점을 수정한다.")
    @PutMapping("")
    public Object updateFeed(@RequestBody final UpdateFeedRequest dto) {
        feedUpdateService.updateFeed(dto);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "모든 피드 목록 조회", notes = "전체 피드 목록 조회, 로그인 회원 번호로 좋아요 여부를 확인한다.")
    @GetMapping("/list/{userId}")
    public Object feedList(@PathVariable final Long userId) {
        final List<FeedResponse> response = feedSearchService.findAll(userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "유저 작성 피드 목록 조회", notes = "특정 유저가 작성한 피드 목록 조회, 로그인 회원 번호로 좋아요 여부를 확인한다.")
    @GetMapping("/list/{authorId}/{userId}")
    public Object userFeedList(@PathVariable final Long authorId, @PathVariable final Long userId) {
        final List<FeedResponse> response = feedSearchService.findByAuthor(authorId, userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "유저 좋아요 피드 목록 조회", notes = "로그인 유저가 좋아요 누른 피드 목록을 조회한다.")
    @GetMapping("/list/like/{userId}")
    public Object userLikeFeedList(@PathVariable final Long userId) {
        final List<FeedResponse> response = feedSearchService.findByLike(userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "단일 피드 조회", notes = "피드 번호로 피드 조회, 회원 번호로 좋아요 여부를 확인한다.")
    @GetMapping("/{feedId}/{userId}")
    public Object feedInfo(@PathVariable final Long feedId, @PathVariable final Long userId) {
        final FeedResponse response = feedSearchService.findById(feedId, userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "피드 삭제", notes = "피드 번호로 피드를 삭제한다.")
    @DeleteMapping("/{feedId}")
    public Object deleteFeed(@PathVariable final Long feedId) {
        feedDeleteService.deleteFeed(feedId);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "피드 좋아요", notes = "피드 좋아요를 누른다.")
    @PostMapping("/like")
    public Object likeFeed(@RequestBody final LikeFeedRequest dto) {
        feedLikeService.doLikeFeed(dto);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "피드 좋아요 취소", notes = "피드 좋아요를 취소한다.")
    @DeleteMapping("/like/{feedId}/{userId}")
    public Object cancelLikeFeed(@PathVariable final Long feedId, @PathVariable final Long userId) {
        feedLikeService.doCancelLikeFeed(feedId, userId);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }
}