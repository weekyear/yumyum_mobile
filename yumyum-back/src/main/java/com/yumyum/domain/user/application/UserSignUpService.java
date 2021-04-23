package com.yumyum.domain.user.application;

import com.yumyum.domain.feed.application.FileUploadService;
import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.dto.SignUpRequest;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import com.yumyum.domain.user.exception.EmailDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignUpService {

    private final UserDao userDao;
    private final FileUploadService fileUploadService;
    private final RegexChecker regexChecker;

    public UserResponse doSignUp(final SignUpRequest dto, final MultipartFile file) {
        // 이메일, 별명, 패스워드 비어있는지 확인
        regexChecker.stringCheck("Email", dto.getEmail());
        regexChecker.stringCheck("Nickname", dto.getNickname());
        regexChecker.stringCheck("Introduction", dto.getIntroduction());

        // 프로필 이미지 저장 후 경로 받기, 사진이 등록되지 않았을 경우 빈 문자열 저장
        String profilePath = "";
        if(file == null){
            profilePath = "";
        }else{
            profilePath = fileUploadService.upload(file);
        }

        // 이메일 중복 체크
        if (userDao.existsByEmail(dto.getEmail())) {
            throw new EmailDuplicateException();
        }

        final LocalDateTime nowTime = LocalDateTime.now();
        final User user = userDao.save(dto.toEntity(profilePath, nowTime));
        return new UserResponse(user);
    }
}
