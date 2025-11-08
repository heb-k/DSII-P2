package com.moviez.DSII_P2.service;

import org.springframework.stereotype.Service;
import com.moviez.DSII_P2.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }
}