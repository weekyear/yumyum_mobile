package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.dto.ChangePasswordRequest;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserChangePasswordService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public Object doChangePassword(final ChangePasswordRequest dto){
        final Optional<User> user = userDao.findByEmail(dto.getEmail());
        final String password = dto.getPassword();

        if(!user.isPresent()){
            return HttpUtils.makeResponse("404", null, "user not found", HttpStatus.NOT_FOUND);
        }

        final String encodePassword = passwordEncoder.encode(password);

        user.get().updatePassword(encodePassword);
        return HttpUtils.makeResponse("200", new UserResponse(user.get()), "success", HttpStatus.OK);
    }
}
