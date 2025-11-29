package com.moviez.DSII_P2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.moviez.DSII_P2.config.ExternalApiConfig;
import com.moviez.DSII_P2.model.Review;
import com.moviez.DSII_P2.model.user.User;
import com.moviez.DSII_P2.repository.UserRepository;
import com.moviez.DSII_P2.service.MovieService;
import com.moviez.DSII_P2.service.ReviewService;

@Controller
public class MoviesController {

    private final MovieService movieService;
    private final ExternalApiConfig api;
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    public MoviesController(MovieService movieService, ExternalApiConfig api, ReviewService reviewService, UserRepository userRepository) {
        this.movieService = movieService;
        this.api = api;
        this.reviewService = reviewService;
        this.userRepository = userRepository;
    }

    @GetMapping("/movies")
    public String listarFilmes(@RequestParam(required = false) String nome, Model model) {
        
        // Check if user needs to set username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User user = userRepository.findByLogin(auth.getName());
            if (user != null) {
                if (user.getUsernameField() == null || user.getUsernameField().isEmpty()) {
                    model.addAttribute("needsUsername", true);
                } else {
                    model.addAttribute("needsUsername", false);
                    model.addAttribute("currentUsername", user.getUsernameField());
                }
            } else {
                model.addAttribute("needsUsername", false);
            }
        } else {
            model.addAttribute("needsUsername", false);
        }

        if (nome != null && !nome.isBlank()) {

            String url = api.getBaseUrl()
                    + "/search/movie?api_key=" + api.getApiKey()
                    + "&language=pt-BR&query=" + nome;

            model.addAttribute("searchResults", movieService.getMoviesList(url));
            model.addAttribute("searching", true);

        } else {

            model.addAttribute("popularMovies",
                    movieService.getMoviesList(api.getBaseUrl() + "/movie/popular?api_key=" + api.getApiKey() + "&language=pt-BR"));

            model.addAttribute("topRatedMovies",
                    movieService.getMoviesList(api.getBaseUrl() + "/movie/top_rated?api_key=" + api.getApiKey() + "&language=pt-BR"));

            model.addAttribute("nowPlayingMovies",
                    movieService.getMoviesList(api.getBaseUrl() + "/movie/now_playing?api_key=" + api.getApiKey() + "&language=pt-BR"));

            model.addAttribute("searching", false);
        }

        // Add recent reviews for the new tab
        model.addAttribute("recentReviews", reviewService.getRecentReviews(20));

        return "movies";
    }

    @GetMapping("/movies/{id}")
    public String detalhesFilme(@PathVariable Long id, Model model) {

        model.addAttribute("filme", movieService.getMovieById(id));
        model.addAttribute("reviews", reviewService.getReviewsForMovie(id));
        model.addAttribute("newReview", new Review());

        // Hide review form if user already reviewed this movie
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean alreadyReviewed = false;
        String currentUserId = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            User me = userRepository.findByLogin(auth.getName());
            if (me != null) {
                currentUserId = me.getId();
                alreadyReviewed = reviewService.hasUserReviewed(id, me.getId());
            }
        }
        model.addAttribute("alreadyReviewed", alreadyReviewed);
        model.addAttribute("currentUserId", currentUserId);

        return "movies/detail";
    }
}
