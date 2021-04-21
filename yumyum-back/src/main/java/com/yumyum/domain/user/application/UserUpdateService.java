package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.UpdateRequest;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserFindDao userFindDao;
    private final RegexChecker regexChecker;

    public UserResponse updateUser(final UpdateRequest dto){
        // 이메일, 별명, 패스워드 비어있는지 확인
        regexChecker.stringCheck("Email", dto.getEmail());
        regexChecker.stringCheck("Nickname", dto.getNickname());
        regexChecker.stringCheck("Introduction", dto.getIntroduction());

        // 기본 경로로 전환
        if(dto.getProfilePath() == null){
            dto.setProfilePath("");
        }

        final User user = userFindDao.findByEmail(dto.getEmail());
        user.updateUser(dto);
        return new UserResponse(user);
    }
}
