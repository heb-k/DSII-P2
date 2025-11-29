package com.moviez.DSII_P2.service;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.moviez.DSII_P2.model.Movie;
import com.moviez.DSII_P2.model.Review;
import com.moviez.DSII_P2.model.user.User;
import com.moviez.DSII_P2.repository.MovieRepository;
import com.moviez.DSII_P2.repository.ReviewRepository;
import com.moviez.DSII_P2.repository.UserRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final MovieRepository movieRepo;
    private final MovieService movieService; // usa a API
    private final UserRepository userRepo;

    public ReviewService(ReviewRepository reviewRepo, MovieRepository movieRepo, MovieService movieService, UserRepository userRepo) {
        this.reviewRepo = reviewRepo;
        this.movieRepo = movieRepo;
        this.movieService = movieService;
        this.userRepo = userRepo;
    }

    // ✔ Busca o filme do banco OU cria usando a API
    private Movie getOrCreateMovie(Long movieId) {

        return movieRepo.findById(movieId).orElseGet(() -> {
            Movie apiMovie = movieService.getMovieById(movieId);

            Movie saved = new Movie();
            saved.setId(apiMovie.getId());
            saved.setTitle(apiMovie.getTitle());
            saved.setOverview(apiMovie.getOverview());
            saved.setPosterPath(apiMovie.getPosterPath());
            saved.setReleaseDate(apiMovie.getReleaseDate());
            saved.setVoteAverage(apiMovie.getVoteAverage());

            return movieRepo.save(saved);
        });
    }

    public Review createReview(Long movieId, Review review) {
        Movie movie = getOrCreateMovie(movieId);

        review.setMovie(movie);
        
        // Obter usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepo.findByLogin(username);
            review.setUser(user);
        }

        return reviewRepo.save(review);
    }

    public List<Review> getReviewsForMovie(Long movieId) {
        return reviewRepo.findByMovieId(movieId);
    }
}
