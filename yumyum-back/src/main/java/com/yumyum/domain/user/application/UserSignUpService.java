package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.dto.SignUpRequest;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignUpService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public Object doSignUp(final SignUpRequest dto) {
        final String email = dto.getEmail();
        final String nickname = dto.getEmail();
        final String password = dto.getPassword();

        // 이메일 중복 체크
        if (userDao.findByEmail(email).isPresent()) {
            return HttpUtils.makeResponse("400", null, "this email already exists", HttpStatus.BAD_REQUEST);
        }
        // 이메일, 별명, 패스워드 비어있는지 확인
        if ("".equals(email) || "".equals(nickname) || "".equals(password))
            return HttpUtils.makeResponse("400", null, "data is blank", HttpStatus.BAD_REQUEST);

        // 별명 체크
        if (userDao.getUserByNickname(nickname) != null)
            return HttpUtils.makeResponse("400", null, "this nickname already exists", HttpStatus.BAD_REQUEST);

        final String encodePassword = passwordEncoder.encode(password);
        final LocalDateTime nowTime = LocalDateTime.now();
        final User user = userDao.save(dto.toEntity(encodePassword, nowTime));
        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(user), "success", HttpStatus.OK);
    }
}
