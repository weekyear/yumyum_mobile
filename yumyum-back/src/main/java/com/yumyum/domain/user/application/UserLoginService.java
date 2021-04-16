package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
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

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLoginService {

    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse doLogin(final LoginRequest dto){
        final Optional<User> user = userDao.findByEmail(dto.getEmail());

        if (!passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
            throw new PasswordWrongException();
        }

        final String token = jwtTokenProvider.createToken(String.valueOf(user.get().getId()), user.get().getRoles());
        LoginResponse response = new LoginResponse(new UserResponse(user.get()), token);
        return response;
    }
}
