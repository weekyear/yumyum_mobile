package com.yumyum.domain.user.api;

import com.yumyum.domain.user.application.*;
import com.yumyum.domain.user.dao.UserDeleteDao;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.AuthenticationRequest;
import com.yumyum.domain.user.dto.ChangePasswordRequest;
import com.yumyum.domain.user.dto.SignUpRequest;
import com.yumyum.domain.user.dto.UpdateRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFindDao userFindDao;
    private final UserDeleteDao userDeleteDao;
    private final UserSignUpService userSignUpService;
    private final UserLoginService userLoginService;
    private final UserChangePasswordService userChangePasswordService;
    private final UserUpdateService userUpdateService;
    private final UserLikeFeedsService userLikeFeedsService;

    @ApiOperation(value = "회원가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public Object createMember(
            @Valid @RequestBody @ApiParam(value = "회원가입 시 필요한 회원정보(이메일, 별명, 비밀번호).", required = true) final SignUpRequest dto) {
        return userSignUpService.doSignUp(dto);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "아이디와 비밀번호를 받아 로그인을 합니다.")
    public Object login(
            @RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) final AuthenticationRequest dto) {
        return userLoginService.doLogin(dto);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @PutMapping("/password")
    @ApiOperation(value = "비밀번호 변경")
    public Object changePassword(
            @Valid @RequestBody @ApiParam(value = "비밀번호 변경 시 필요한 회원정보(이메일, 기존 비밀번호, 새 비밀번호).", required = true)
            final ChangePasswordRequest dto) {
        return userChangePasswordService.doChangePassword(dto);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @PutMapping("")
    @ApiOperation(value = "회원 수정")
    public Object update(
            @Valid @RequestBody @ApiParam(value = "회원 정보 수정(닉네임, 한줄 소개).", required = true) final UpdateRequest dto) {
        return userUpdateService.updateUser(dto);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    @ApiOperation(value = "회원 조회")
    public Object getDetailInfo(@Valid @ApiParam(value = "회원 정보 조회", required = true) @PathVariable Long id) {
        return userFindDao.findById(id);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @DeleteMapping("")
    @ApiOperation(value = "회원 삭제")
    public Object delete(
            @Valid @RequestBody @ApiParam(value = "회원정보 탈퇴 시 필요한 회원정보(이메일, 별명, 비밀번호).", required = true) final SignUpRequest dto) {
        return userDeleteDao.deleteByUser(dto);
    }

    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    @GetMapping("{email}/likeFeeds")
    @ApiOperation(value = "현재 좋아요한 Feed 조회")
    public Object getLikeFeeds(@Valid @ApiParam(value = "회원 정보 조회", required = true) @PathVariable final String email) {
        return userLikeFeedsService.getLikeFeeds(email);
    }
}