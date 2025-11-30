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

            // Enforce single review per user per movie
            if (user != null && reviewRepo.existsByMovieIdAndUserId(movieId, user.getId())) {
                // Return existing review without creating a duplicate
                return reviewRepo.findByMovieIdAndUserId(movieId, user.getId());
            }
        }

        return reviewRepo.save(review);
    }

    public List<Review> getReviewsForMovie(Long movieId) {
        return reviewRepo.findByMovieId(movieId);
    }

    // New: get reviews by user id (for profile page)
    public List<Review> getReviewsByUser(String userId) {
        return reviewRepo.findByUserId(userId);
    }

    public boolean hasUserReviewed(Long movieId, String userId) {
        if (userId == null) return false;
        return reviewRepo.existsByMovieIdAndUserId(movieId, userId);
    }

    // Update review
    public Review updateReview(Long reviewId, String comment, Double rating) {
        Review review = reviewRepo.findById(reviewId).orElseThrow(() -> 
            new RuntimeException("Review não encontrada"));
        
        // Verify ownership
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepo.findByLogin(username);
            if (user != null && review.getUser().getId().equals(user.getId())) {
                review.setComment(comment);
                review.setRating(rating);
                return reviewRepo.save(review);
            }
        }
        throw new RuntimeException("Não autorizado");
    }

    // Delete review
    public void deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId).orElseThrow(() -> 
            new RuntimeException("Review não encontrada"));
        
        // Verify ownership or admin role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepo.findByLogin(username);
            
            // Check if user is admin
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            // Allow deletion if user is admin (even if review has no owner)
            if (isAdmin) {
                reviewRepo.delete(review);
                return;
            }

            // Otherwise, allow deletion only if the authenticated user owns the review
            if (user != null && review.getUser() != null && review.getUser().getId().equals(user.getId())) {
                reviewRepo.delete(review);
                return;
            }
        }
        throw new RuntimeException("Não autorizado");
    }

    // Get review by ID
    public Review getReviewById(Long reviewId) {
        return reviewRepo.findById(reviewId).orElse(null);
    }

    // Get recent reviews
    public List<Review> getRecentReviews(int limit) {
        return reviewRepo.findAll().stream()
            .filter(r -> r.getCreatedAt() != null)
            .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
            .limit(limit)
            .toList();
    }
}
