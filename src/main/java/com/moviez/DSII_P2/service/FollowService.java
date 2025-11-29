package com.moviez.DSII_P2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moviez.DSII_P2.model.user.User;
import com.moviez.DSII_P2.model.user.UserFollow;
import com.moviez.DSII_P2.repository.UserFollowRepository;

@Service
public class FollowService {

    private final UserFollowRepository followRepo;

    public FollowService(UserFollowRepository followRepo) {
        this.followRepo = followRepo;
    }

    @Transactional
    public boolean follow(User follower, User target) {
        if (follower == null || target == null) return false;
        if (follower.getId().equals(target.getId())) return false; // no self-follow
        if (followRepo.existsByFollowerAndFollowing(follower, target)) return true; // already following
        followRepo.save(new UserFollow(follower, target));
        return true;
    }

    @Transactional
    public boolean unfollow(User follower, User target) {
        if (follower == null || target == null) return false;
        List<UserFollow> links = followRepo.findByFollowerId(follower.getId());
        boolean removed = false;
        for (UserFollow uf : links) {
            if (uf.getFollowing().getId().equals(target.getId())) {
                followRepo.delete(uf);
                removed = true;
            }
        }
        return removed;
    }

    public long countFollowers(User user) {
        return followRepo.countByFollowingId(user.getId());
    }

    public long countFollowing(User user) {
        return followRepo.countByFollowerId(user.getId());
    }

    public List<UserFollow> getFollowers(User user) {
        return followRepo.findByFollowingId(user.getId());
    }

    public List<UserFollow> getFollowing(User user) {
        return followRepo.findByFollowerId(user.getId());
    }

    public boolean isFollowing(User follower, User target) {
        if (follower == null || target == null) return false;
        if (follower.getId().equals(target.getId())) return false;
        return followRepo.existsByFollowerAndFollowing(follower, target);
    }
}
