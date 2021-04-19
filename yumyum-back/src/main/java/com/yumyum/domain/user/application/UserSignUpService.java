package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.dto.SignUpRequest;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import com.yumyum.domain.user.exception.EmailDuplicateException;
import lombok.RequiredArgsConstructor;
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
    private final RegexChecker regexChecker;

    public UserResponse doSignUp(final SignUpRequest dto) {
        final String email = dto.getEmail();
        final String password = dto.getPassword();

        // 이메일, 별명, 패스워드 비어있는지 확인
        regexChecker.stringCheck("Email", email);
        regexChecker.stringCheck("Nickname", dto.getNickname());
        regexChecker.stringCheck("Password", password);
        // 이메일 중복 체크
        if (userDao.existsByEmail(email)) {
            throw new EmailDuplicateException();
        }

        final String encodePassword = passwordEncoder.encode(password);
        final LocalDateTime nowTime = LocalDateTime.now();
        final User user = userDao.save(dto.toEntity(encodePassword, nowTime));
        return new UserResponse(user);
    }
}
