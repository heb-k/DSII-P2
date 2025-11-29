package com.moviez.DSII_P2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalApiConfig {
	
	@Value("https://api.themoviedb.org/3")
    private String baseUrl;

    @Value("e02b18911bb6f474a627f6d9111c1de9")
    private String apiKey;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
	//private final String API_KEY = "e02b18911bb6f474a627f6d9111c1de9";
    //private final String BASE_URL = "https://api.themoviedb.org/3";

}
