package com.moviez.DSII_P2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviez.DSII_P2.model.user.User;
import com.moviez.DSII_P2.model.user.UserFollow;

public interface UserFollowRepository extends JpaRepository<UserFollow, String> {
    boolean existsByFollowerAndFollowing(User follower, User following);
    long countByFollowerId(String followerId);
    long countByFollowingId(String followingId);
    List<UserFollow> findByFollowerId(String followerId);
    List<UserFollow> findByFollowingId(String followingId);
}
