package com.moviez.DSII_P2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviez.DSII_P2.model.user.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByLogin(String login);
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
