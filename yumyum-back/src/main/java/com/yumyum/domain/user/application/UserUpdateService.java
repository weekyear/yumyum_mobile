package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.UpdateRequest;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserFindDao userFindDao;

    public UserResponse updateUser(final UpdateRequest dto){
        final User user = userFindDao.findByEmail(dto.getEmail());
        user.updateUser(dto);
        return new UserResponse(user);
    }
}
