package com.yumyum.domain.user.application;

import com.yumyum.domain.user.dao.FollowDao;
import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.dto.FollowRequest;
import com.yumyum.domain.user.entity.Follow;
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
public class UserFollowService {

    private final UserDao userDao;
    private final FollowDao followDao;

    public Object doFollowUser(final FollowRequest dto){
        final Optional<User> host = userDao.findById(dto.getHostId());
        if (!host.isPresent()) {
            return HttpUtils.makeResponse("404", null, "host not found", HttpStatus.NOT_FOUND);
        }

        final Optional<User> follower = userDao.findById(dto.getHostId());
        if (!follower.isPresent()) {
            return HttpUtils.makeResponse("404", null, "follower not found", HttpStatus.NOT_FOUND);
        }

        followDao.save(dto.toEntity(host.get(), follower.get()));
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    public Object doCancelFollowUser(final Long hostId, final Long followerId){
        final Optional<Follow> follow = followDao.findByHostIdAndFollowerId(hostId, followerId);
        if (!follow.isPresent()) {
            return HttpUtils.makeResponse("404", null, "follow not found", HttpStatus.NOT_FOUND);
        }

        followDao.delete(follow.get());
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }
}
