package com.yumyum.domain.user.dao;

import com.yumyum.domain.user.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowDao extends JpaRepository<Follow, Long> {

    List<Follow> findByHostId(Long hostId);

    List<Follow> findByFollowerId(Long followerId);

    Optional<Follow> findByHostIdAndFollowerId(Long hostId, Long followerId);
}
