package com.moviez.DSII_P2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.moviez.DSII_P2.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
