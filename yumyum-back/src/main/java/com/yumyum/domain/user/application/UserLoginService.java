package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.dto.LoginRequest;
import com.yumyum.domain.user.dto.LoginResponse;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.HttpUtils;
import com.yumyum.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public Object doLogin(final LoginRequest dto){
        final String email = dto.getEmail();
        final String password = dto.getPassword();
        final Optional<User> user = userDao.findByEmail(email);

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return HttpUtils.makeResponse("400", null, "mismatch", HttpStatus.BAD_REQUEST);
        }

        final String token = jwtTokenProvider.createToken(String.valueOf(user.get().getId()), user.get().getRoles());
        LoginResponse response = new LoginResponse(new UserResponse(user.get()), token);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }
}
