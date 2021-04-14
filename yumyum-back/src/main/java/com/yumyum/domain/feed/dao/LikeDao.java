package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeDao extends JpaRepository<Like, Long> {
    List<Like> findByUserEmailAndFeedId(String email, Long feedId);

    List<Like> findByFeedIdAndUserId(Long feedId, Long userId);

    List<Like> findAllById(Long id);

    List<Like> findAllByUserEmail(String userEmail);
}