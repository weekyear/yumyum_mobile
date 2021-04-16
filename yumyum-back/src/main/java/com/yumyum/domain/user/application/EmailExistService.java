package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailExistService {

    private final UserDao userDao;

    public Object checkEmailExist(final String email){
        final Boolean exist = userDao.existsByEmail(email);
        return HttpUtils.makeResponse("200", exist, "success", HttpStatus.OK);
    }
}
