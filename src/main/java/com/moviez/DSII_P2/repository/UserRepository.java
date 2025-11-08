package com.moviez.DSII_P2.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.moviez.DSII_P2.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}