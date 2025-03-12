package com.rb.follow.service;

import com.rb.follow.client.UserClient;
import com.rb.follow.dto.FollowUser;
import com.rb.follow.dto.TypeOfAction;
import com.rb.follow.dto.User;
import com.rb.follow.entity.Follow;
import com.rb.follow.exception.FollowException;
import com.rb.follow.exception.UserException;
import com.rb.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserClient userClient;
    private final NotificationService notificationService;

    @Override
    public Follow follow(String loggedInUser, String followingUsername) {
        checkUserExistence(loggedInUser);
        checkUserExistence(followingUsername);
        if (loggedInUser.equals(followingUsername)) {
            log.info("{} trying to follow {}, User can not follow him/her self", loggedInUser, followingUsername);
            throw new FollowException("You are not allowed to follow yourself");
        } else {
            Optional<Follow> optionalFollow = followRepository.findByFollowerIdAndFollowingId(loggedInUser, followingUsername);
            if (optionalFollow.isEmpty()) {
                log.info("{} can follow {}", loggedInUser, followingUsername);
                Follow follow = new Follow();
                follow.setFollowerId(loggedInUser);
                follow.setFollowingId(followingUsername);
                notificationService.publishFollowEvent(loggedInUser,followingUsername, TypeOfAction.FOLLOW);
                return followRepository.save(follow);
            }
            log.info("User {} already following {}", loggedInUser, followingUsername);
            throw new FollowException("You are already following to : " + followingUsername);
        }
    }

    @Override
    public void unFollow(String loggedInUsername,String followingUsername) {
        checkUserExistence(followingUsername);
        checkUserExistence(loggedInUsername);
        long numberOfFollowsDeleted = followRepository.deleteByFollowerIdAndFollowingId(loggedInUsername, followingUsername);
        if (numberOfFollowsDeleted > 0) {
            notificationService.publishFollowEvent(loggedInUsername,followingUsername, TypeOfAction.UNFOLLOW);
            log.info("Follow deleted successfully");
        } else {
            log.info("No following history found to delete");
            throw new FollowException("No follow found");
        }
    }

    @Override
    public List<FollowUser> getFollowers(String username) {
        checkUserExistence(username);
        Optional<List<Follow>> optionalFollowers = followRepository.findByFollowingId(username);
        if (optionalFollowers.isPresent() && optionalFollowers.get().size() > 0) {
            List<Follow> follows = optionalFollowers.get();
            log.info("{} followers found to {}", follows.size(), username);
            return getFollowUserFromFollows(follows);
        } else {
            log.info("No followers found : {}", username);
            throw new FollowException("No one following : " + username + " yet");
        }
    }

    @Override
    public List<FollowUser> getFollowings(String username) {
        checkUserExistence(username);
        Optional<List<Follow>> optionalFollowings = followRepository.findByFollowerId(username);
        if (optionalFollowings.isPresent() && !optionalFollowings.get().isEmpty()) {
            List<Follow> follows = optionalFollowings.get();
            log.info("{} followings found to {}", follows.size(), username);
            return getFollowUserFromFollows(follows);
        } else {
            log.info("No followings found : {}", username);
            throw new FollowException("You have not followed anyone");
        }
    }

    private void checkUserExistence(String username) {
        ResponseEntity<User> userResponseEntity = userClient.searchUser(username);
        if (userResponseEntity.getStatusCode() == HttpStatus.OK) {
            log.info("User found : {}", username);
        } else {
            log.info("No user found with username : {}", username);
            throw new UserException("No user found with username : " + username);
        }
    }

    private List<FollowUser> getFollowUserFromFollows(List<Follow> follows) {
        return follows.stream().map(f -> userClient.searchUser(f.getFollowerId()))
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
