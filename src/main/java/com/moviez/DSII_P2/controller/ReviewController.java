package com.moviez.DSII_P2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.moviez.DSII_P2.model.Review;
import com.moviez.DSII_P2.service.ReviewService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create/{movieId}")
    public String createReview(@PathVariable Long movieId, Review review) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/auth/login";
        }
        reviewService.createReview(movieId, review);
        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/update/{reviewId}")
    public String updateReview(@PathVariable Long reviewId, 
                              @RequestParam String comment, 
                              @RequestParam Double rating,
                              @RequestParam Long movieId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/auth/login";
        }
        try {
            reviewService.updateReview(reviewId, comment, rating);
            return "redirect:/movies/" + movieId;
        } catch (RuntimeException e) {
            return "redirect:/movies/" + movieId + "?error=unauthorized";
        }
    }

    @PostMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId, @RequestParam Long movieId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/auth/login";
        }
        try {
            reviewService.deleteReview(reviewId);
            return "redirect:/movies/" + movieId;
        } catch (RuntimeException e) {
            return "redirect:/movies/" + movieId + "?error=unauthorized";
        }
    }
}
