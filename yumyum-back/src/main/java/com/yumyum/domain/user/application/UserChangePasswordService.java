package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.ChangePasswordRequest;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserChangePasswordService {

    private final UserFindDao userFindDao;
    private final PasswordEncoder passwordEncoder;

    public void doChangePassword(final ChangePasswordRequest dto){
        final User user = userFindDao.findByEmail(dto.getEmail());
        final String encodePassword = passwordEncoder.encode(dto.getPassword());
        user.updatePassword(encodePassword);
    }
}
