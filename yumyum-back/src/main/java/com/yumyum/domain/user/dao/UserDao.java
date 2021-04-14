package com.yumyum.domain.user.dao;

import com.yumyum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User getUserByNickname(String nickname);
    String getSaltByEmail(String email);
    Optional<User> findUserByEmailAndPassword(String email, String password);

    List<User> findByNicknameContaining(String nickname);
}
