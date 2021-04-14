package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public UserDetails loadUserByUsername(String userPk) {
        return userDao.findById(Long.valueOf(userPk)).get();
    }
}