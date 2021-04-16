package com.yumyum.domain.user.dao;

import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.Follow;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowFindDao {

    private final UserDao userDao;
    private final FollowDao followDao;

    public Object getFollowList(final Long id, final String listType){
        final Optional<User> user = userDao.findById(id);
        if (!user.isPresent()) {
            return HttpUtils.makeResponse("404", null, "user not found", HttpStatus.NOT_FOUND);
        }

        final List<UserResponse> list = entityToDto(id, listType);
        return HttpUtils.makeResponse("200", list, "success", HttpStatus.OK);
    }

    public List<UserResponse> entityToDto(final Long id, final String listType){
        List<UserResponse> list = new ArrayList<>();

        switch (listType){
            case "host":
                final List<Follow> hList = followDao.findByFollowerId(id); // 팔로우 중인 리스트
                for(Follow follow : hList){
                    Optional<User> user = userDao.findById(follow.getHost().getId());
                    list.add(new UserResponse(user.get()));
                }
                break;
            case "follower":
                final List<Follow> fList = followDao.findByHostId(id); // 팔로워 리스트
                for(Follow follow : fList){
                    Optional<User> user = userDao.findById(follow.getFollower().getId());
                    list.add(new UserResponse(user.get()));
                }
                break;
            default:
                break;
        }

        return list;
    }
}
