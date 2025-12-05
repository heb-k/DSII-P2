package com.moviez.DSII_P2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalApiConfig {
	
	@Value("https://api.themoviedb.org/3")
    private String baseUrl;

    @Value("${api.tmdb.key}")
    private String apiKey;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
}
