package com.moviez.DSII_P2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moviez.DSII_P2.model.user.User;
import com.moviez.DSII_P2.repository.UserRepository;
import com.moviez.DSII_P2.repository.UserFollowRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AccountController {

    private final UserRepository userRepository;
    private final UserFollowRepository followRepository;

    public AccountController(UserRepository userRepository, UserFollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    // Realtime availability (JSON) - reuse existing but namespaced for future
    @GetMapping("/users/check-username")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        boolean available = !userRepository.existsByUsername(username);
        Map<String, Boolean> resp = new HashMap<>();
        resp.put("available", available);
        return ResponseEntity.ok(resp);
    }

    // Update own username (form submit)
    @PostMapping("/users/update-username")
    @Transactional
    public String updateUsername(@RequestParam String username, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/auth/login";
        }
        String login = auth.getName();
        User me = userRepository.findByLogin(login);
        if (me == null) {
            return "redirect:/movies";
        }
        // Basic validation
        if (username == null || username.length() < 3 || username.length() > 20 || !username.matches("[a-zA-Z0-9_]+")) {
            return "redirect:/profile?username_error=invalid";
        }
        if (userRepository.existsByUsername(username)) {
            return "redirect:/profile?username_error=exists";
        }
        me.setUsername(username);
        userRepository.save(me);
        return "redirect:/users/" + username + "?username_changed=1";
    }

    // Delete own account
    @PostMapping("/users/delete-account")
    @Transactional
    public String deleteAccount(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/auth/login";
        }
        User me = userRepository.findByLogin(auth.getName());
        if (me != null) {
            // Remove follow relations where user is follower or following
            followRepository.findByFollowerId(me.getId()).forEach(followRepository::delete);
            followRepository.findByFollowingId(me.getId()).forEach(followRepository::delete);
            userRepository.delete(me); // reviews removed via orphanRemoval
        }
        new SecurityContextLogoutHandler().logout(request, response, auth);
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
