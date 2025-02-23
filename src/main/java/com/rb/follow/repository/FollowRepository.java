package com.rb.follow.repository;

import com.rb.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,String> {
    Optional<Follow> findByFollowerIdAndFollowingId(String followerId,String followingId);
    @Transactional
    long deleteByFollowerIdAndFollowingId(String followerId,String followingId);
    Optional<List<Follow>> findByFollowingId(String followerId);
    Optional<List<Follow>> findByFollowerId(String followerId);
}
