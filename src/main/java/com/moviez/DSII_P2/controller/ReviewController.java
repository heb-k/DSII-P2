package com.moviez.DSII_P2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
        reviewService.createReview(movieId, review);
        return "redirect:/movies/" + movieId;
    }
}
