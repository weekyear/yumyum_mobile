package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.LoginRequest;
import com.yumyum.domain.user.dto.LoginResponse;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import com.yumyum.domain.user.exception.PasswordWrongException;
import com.yumyum.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLoginService {

    private final UserFindDao userFindDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RegexChecker regexChecker;

    public LoginResponse doLogin(final LoginRequest dto){
        final User user = userFindDao.findByEmail(dto.getEmail());

        regexChecker.stringCheck("Password", dto.getPassword());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new PasswordWrongException();
        }

        final String token = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles());
        return new LoginResponse(new UserResponse(user), token);
    }
}
