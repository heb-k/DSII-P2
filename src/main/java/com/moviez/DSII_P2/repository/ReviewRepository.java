package com.moviez.DSII_P2.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moviez.DSII_P2.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMovieId(Long movieId);
    
    List<Review> findByUserId(String userId);
}
