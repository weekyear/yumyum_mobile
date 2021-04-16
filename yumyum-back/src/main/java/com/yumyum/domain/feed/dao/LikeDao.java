package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeDao extends JpaRepository<Like, Long> {

    List<Like> findByUserEmailAndFeedId(String email, Long feedId);

    Optional<Like> findByFeedIdAndUserId(Long feedId, Long userId);

    List<Like> findAllById(Long id);

    List<Like> findAllByUserEmail(String userEmail);

    Long countById(Long feedId);

    List<Like> findByUserId(Long userId);
}