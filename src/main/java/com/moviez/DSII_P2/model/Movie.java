package com.moviez.DSII_P2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Movie {

    @Id
    private Long id;

    private String title;
    
    //@Lob
    @Column(columnDefinition = "TEXT")
    private String overview;

    private String posterPath;
    private String releaseDate;
    private Double voteAverage;
}
