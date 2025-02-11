package com.rb.follow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rb.follow.client.UserClient;
import com.rb.follow.dto.FollowRequest;
import com.rb.follow.dto.FollowUser;
import com.rb.follow.dto.User;
import com.rb.follow.entity.Follow;
import com.rb.follow.exception.FollowException;
import com.rb.follow.exception.UserException;
import com.rb.follow.repository.FollowRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserClient userClient;
    private final ObjectMapper objectMapper;

    @Override
    public Follow follow(FollowRequest followRequest) {
        String follower = followRequest.getFollowerId(), following = followRequest.getFollowingId();
        checkUserExistence(follower);
        checkUserExistence(following);
        if (follower.equals(following)) {
            log.info("{} trying to follow {}, User can not follow him/her self", follower, following);
            throw new FollowException("You are not allowed to follow yourself");
        } else {
            Optional<Follow> optionalFollow = followRepository.findByFollowerIdAndFollowingId(follower, following);
            if (optionalFollow.isEmpty()) {
                log.info("{} can follow {}", follower, following);
                Follow follow = objectMapper.convertValue(followRequest, Follow.class);
                return followRepository.save(follow);
            }
            log.info("User {} already following {}", follower, following);
            throw new FollowException("You are already following to : " + following);
        }
    }

    @Override
    public void unFollow(FollowRequest followRequest) {
        String follower = followRequest.getFollowerId(), following = followRequest.getFollowingId();
        checkUserExistence(follower);
        checkUserExistence(following);
        long numberOfFollowsDeleted = followRepository.deleteByFollowerIdAndFollowingId(follower, following);
        if (numberOfFollowsDeleted > 0) {
            log.info("Follow deleted successfully");
        } else {
            log.info("No following history found to delete");
            throw new FollowException("No follow found");
        }
    }

    @Override
    public List<FollowUser> getFollowers(String userId) {
        checkUserExistence(userId);
        Optional<List<Follow>> optionalFollowers = followRepository.findByFollowingId(userId);
        if (optionalFollowers.isPresent()) {
            List<Follow> follows = optionalFollowers.get();
            log.info("{} followers found to {}", follows.size(), userId);
            return getFollowUserFromFollows(follows);
        } else {
            log.info("No followers found : {}", userId);
            throw new FollowException("No one following : " + userId + " yet");
        }
    }

    @Override
    public List<FollowUser> getFollowings(String userId) {
        checkUserExistence(userId);
        Optional<List<Follow>> optionalFollowings = followRepository.findByFollowerId(userId);
        if (optionalFollowings.isPresent() && !optionalFollowings.get().isEmpty()) {
            List<Follow> follows = optionalFollowings.get();
            log.info("{} followings found to {}", follows.size(), userId);
            return getFollowUserFromFollows(follows);
        } else {
            log.info("No followings found : {}", userId);
            throw new FollowException("You have not followed anyone");
        }
    }

    private void checkUserExistence(String userId) {
        try {
            userClient.getUserById(userId);
            log.info("User found : {}", userId);
        } catch (FeignException.BadRequest ex) {
            log.info("No user found with id : {}", userId);
            throw new UserException("No user found with id : " + userId);
        }
    }

    private List<FollowUser> getFollowUserFromFollows(List<Follow> follows) {
        return follows.stream().map(f -> userClient.getUserById(f.getFollowerId()))
                .filter(res -> res.getStatusCode().is2xxSuccessful())
                .map(response -> {
                    User actualUser = response.getBody();
                    FollowUser followUser = new FollowUser();
                    followUser.setFirstName(actualUser.getFirstName());
                    followUser.setLastName(actualUser.getLastName());
                    followUser.setUsername(actualUser.getUsername());
                    return followUser;
                })
                .toList();
    }
}
