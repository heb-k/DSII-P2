package com.moviez.DSII_P2.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moviez.DSII_P2.model.user.User;
import com.moviez.DSII_P2.model.user.UserFollow;
import com.moviez.DSII_P2.repository.UserRepository;
import com.moviez.DSII_P2.service.FollowService;

@Controller
public class FollowController {

    private final FollowService followService;
    private final UserRepository userRepository;

    private final com.moviez.DSII_P2.service.ReviewService reviewService;

    public FollowController(FollowService followService, UserRepository userRepository, com.moviez.DSII_P2.service.ReviewService reviewService) {
        this.followService = followService;
        this.userRepository = userRepository;
        this.reviewService = reviewService;
    }

    @PostMapping("/users/{username}/follow")
    public String follow(@PathVariable String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/auth/login";
        }
        User me = userRepository.findByLogin(auth.getName());
        User target = userRepository.findByUsername(username);
        if (target != null) {
            followService.follow(me, target);
        }
        return "redirect:/users/" + username;
    }

    @PostMapping("/users/{username}/unfollow")
    public String unfollow(@PathVariable String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/auth/login";
        }
        User me = userRepository.findByLogin(auth.getName());
        User target = userRepository.findByUsername(username);
        if (target != null) {
            followService.unfollow(me, target);
        }
        return "redirect:/users/" + username;
    }

    @GetMapping("/users/{username}/followers/count")
    @ResponseBody
    public long followersCount(@PathVariable String username) {
        User u = userRepository.findByUsername(username);
        return u == null ? 0 : followService.countFollowers(u);
    }

    @GetMapping("/users/{username}/following/count")
    @ResponseBody
    public long followingCount(@PathVariable String username) {
        User u = userRepository.findByUsername(username);
        return u == null ? 0 : followService.countFollowing(u);
    }

    // Profile page
    @GetMapping("/users/{username}")
    public String userProfile(@PathVariable String username, Model model) {
        User target = userRepository.findByUsername(username);
        if (target == null) return "redirect:/movies";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User me = null;
        boolean authenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName());
        if (authenticated) {
            me = userRepository.findByLogin(auth.getName());
        }

        boolean isFollowing = false;
        boolean isMe = false;
        if (me != null) {
            isMe = me.getId().equals(target.getId());
            isFollowing = followService.isFollowing(me, target);
        }

        long followers = followService.countFollowers(target);
        long following = followService.countFollowing(target);
        model.addAttribute("profileUser", target);
        model.addAttribute("followers", followers);
        model.addAttribute("following", following);
        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("isMe", isMe);
        model.addAttribute("authenticated", authenticated);
        model.addAttribute("reviews", reviewService.getReviewsByUser(target.getId()));
        return "users/profile";
    }

    // Followers and Following page
    @GetMapping("/users/{username}/connections")
    public String userConnections(@PathVariable String username, Model model) {
        User target = userRepository.findByUsername(username);
        if (target == null) return "redirect:/movies";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName());

        List<UserFollow> followersList = followService.getFollowers(target);
        List<UserFollow> followingList = followService.getFollowing(target);

        model.addAttribute("profileUser", target);
        model.addAttribute("followersList", followersList);
        model.addAttribute("followingList", followingList);
        model.addAttribute("authenticated", authenticated);
        return "users/connections";
    }
}
