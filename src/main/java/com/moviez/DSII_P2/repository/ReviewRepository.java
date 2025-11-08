package com.moviez.DSII_P2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.moviez.DSII_P2.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
