package com.rb.follow.repository;

import com.rb.follow.entity.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends MongoRepository<Follow,String> {
    Optional<Follow> findByFollowerIdAndFollowingId(String followerId,String followingId);
    @Transactional
    long deleteByFollowerIdAndFollowingId(String followerId,String followingId);
    Optional<List<Follow>> findByFollowingId(String followerId);
    Optional<List<Follow>> findByFollowerId(String followerId);
}
