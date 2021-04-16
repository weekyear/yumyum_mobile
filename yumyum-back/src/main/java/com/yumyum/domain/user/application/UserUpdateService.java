package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.dto.UpdateRequest;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserDao userDao;

    public Object updateUser(final UpdateRequest dto){
        final Optional<User> user = userDao.findByEmail(dto.getEmail());
        if (!user.isPresent()) {
            return HttpUtils.makeResponse("404", null, "user not found", HttpStatus.NOT_FOUND);
        }

        user.get().updateUser(dto);

        return HttpUtils.makeResponse("200", new UserResponse(user.get()), "success", HttpStatus.OK);
    }
}
