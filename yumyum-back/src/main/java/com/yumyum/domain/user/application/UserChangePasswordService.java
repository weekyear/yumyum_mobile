package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.dto.ChangePasswordRequest;
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
public class UserChangePasswordService {

    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public Object doChangePassword(final ChangePasswordRequest dto){
        final Optional<User> user = userDao.findByEmail(dto.getUserEmail());
        final String password = dto.getPassword();
        final String newPassword = dto.getNewPassword();

        if(!user.isPresent()){
            return HttpUtils.makeResponse("404", null, "user not found", HttpStatus.NOT_FOUND);
        }

        final String encodePassword = passwordEncoder.encode(password);
        final String encodeNewPassword = passwordEncoder.encode(newPassword);

        if(!encodePassword.equals(user.get().getPassword())){
            return HttpUtils.makeResponse("400", null, "password is not match", HttpStatus.BAD_REQUEST);
        }else{
            user.get().updatePassword(encodeNewPassword);
            return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(user), "success", HttpStatus.OK);
        }
    }
}
