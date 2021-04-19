package com.yumyum.domain.user.api;

import com.yumyum.domain.user.application.*;
import com.yumyum.domain.user.dao.UserDeleteDao;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.*;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.Existence;
import com.yumyum.global.common.response.HttpUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFindDao userFindDao;
    private final UserDeleteDao userDeleteDao;
    private final UserSignUpService userSignUpService;
    private final EmailExistService emailExistService;
    private final UserLoginService userLoginService;
    private final UserChangePasswordService userChangePasswordService;
    private final UserUpdateService userUpdateService;
    private final UserFollowService userFollowService;
    private final FollowSearchService followSearchService;

    @ApiOperation(value = "회원가입", notes = "이메일, 비밀번호, 닉네임을 받아 회원가입한다.")
    @PostMapping("/signup")
    public Object signUp(@RequestBody final SignUpRequest dto) {
        final UserResponse response = userSignUpService.doSignUp(dto);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "이메일 중복 확인", notes = "해당 이메일로 가입한 계정이 있는지 확인한다.")
    @GetMapping("/email/{email}")
    public Object getFollowerList(@PathVariable final String email) {
        final Existence response =  emailExistService.checkEmailExist(email);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "로그인", notes = "아이디와 비밀번호를 받아 로그인한다.")
    @PostMapping("/login")
    public Object login(@RequestBody final LoginRequest dto) {
        final LoginResponse response = userLoginService.doLogin(dto);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 변경", notes = "이메일과 새 비밀번호로 비밀번호를 변경한다.")
    @PutMapping("/password")
    public Object changePassword(@RequestBody final ChangePasswordRequest dto) {
        userChangePasswordService.doChangePassword(dto);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "회원 수정", notes = "회원 번호로 닉네임과 한줄 소개를 수정한다.")
    @PutMapping("")
    public Object updateUser(@RequestBody final UpdateRequest dto) {
        final UserResponse response = userUpdateService.updateUser(dto);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "회원 조회", notes = "회원 번호로 회원 조회를 한다.")
    @GetMapping("/{userId}")
    public Object getUserInfo(@PathVariable final Long userId) {
        final User user = userFindDao.findById(userId);
        return HttpUtils.makeResponse("200", new UserResponse(user), "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "회원 삭제", notes = "회원 번호로 회원 탈퇴를 한다.")
    @DeleteMapping("/{userId}")
    public Object deleteUser(@PathVariable final Long userId) {
        userDeleteDao.deleteById(userId);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "팔로우 목록 조회", notes = "유저가 팔로우하고 있는 회원 목록을 불러온다.")
    @GetMapping("/follow/list/{userId}")
    public Object getFollowList(@PathVariable final Long userId) {
        final List<UserResponse> response = followSearchService.getFollowList(userId, "host");
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "팔로워 목록 조회", notes = "유저를 팔로우하고 있는 회원 목록을 불러온다.")
    @GetMapping("/follow/follower/{userId}")
    public Object getFollowerList(@PathVariable final Long userId) {
        final List<UserResponse> response = followSearchService.getFollowList(userId, "follower");
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "팔로우", notes = "유저를 팔로우한다.")
    @PostMapping("/follow")
    public Object followUser(@RequestBody final FollowRequest dto) {
        userFollowService.doFollowUser(dto);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "팔로우 취소", notes = "유저 팔로우를 취소한다.")
    @DeleteMapping("/follow/follower/{hostId}/{followerId}")
    public Object cancelFollowUser(@PathVariable final Long hostId, @PathVariable final Long followerId) {
        userFollowService.doCancelFollowUser(hostId, followerId);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }
}