package com.yumyum.domain.user.dao;

import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDeleteDao {

    private final UserDao userDao;
    private final UserFindDao userFindDao;

    public void deleteById(final Long id){
        final User user = userFindDao.findById(id);
        userDao.delete(user);
    }
}
