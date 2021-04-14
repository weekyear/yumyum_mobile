package com.yumyum.domain.user.dao;

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
public class UserFindDao {

    private final UserDao userDao;

    public Object findById(final Long id){
        final Optional<User> user = userDao.findById(id);
        if (!user.isPresent()) {
            return HttpUtils.makeResponse("404", null, "user not found", HttpStatus.NOT_FOUND);
        }
        return HttpUtils.makeResponse("200", HttpUtils.convertObjToJson(user.get()), "success", HttpStatus.OK);
    }
}
