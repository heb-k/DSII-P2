package com.moviez.DSII_P2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviez.DSII_P2.config.MovieClient;
import com.moviez.DSII_P2.config.ExternalApiConfig;
import com.moviez.DSII_P2.model.Movie;

@Service
public class MovieService {
    
    private final ExternalApiConfig apiConfig;
    private final MovieClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public MovieService(ExternalApiConfig apiConfig, MovieClient client) {
        this.apiConfig = apiConfig;
        this.client = client;
    }

    // ---------------------------------------
    // 1) Buscar LISTA de filmes
    // ---------------------------------------
    public List<Movie> getMoviesList(String url) {
        List<Movie> filmes = new ArrayList<>();

        try {
            String resposta = client.get(url);
            JsonNode results = mapper.readTree(resposta).get("results");

            if (results != null) {
                for (JsonNode node : results) {
                    Movie movie = convertJson(node);
                    
                    // Se não tiver overview em pt-BR, busca em inglês como fallback
                    if ((movie.getOverview() == null || movie.getOverview().trim().isEmpty()) 
                        && movie.getId() != null) {
                        try {
                            String urlEn = apiConfig.getBaseUrl()
                                    + "/movie/" + movie.getId()
                                    + "?api_key=" + apiConfig.getApiKey()
                                    + "&language=en-US";
                            
                            String respostaEn = client.get(urlEn);
                            JsonNode nodeEn = mapper.readTree(respostaEn);
                            
                            String titleEn = nodeEn.path("title").asText("");
                            String overviewEn = nodeEn.path("overview").asText("");
                            
                            if (!titleEn.isEmpty()) {
                                movie.setTitle(titleEn);
                            }
                            if (!overviewEn.isEmpty()) {
                                movie.setOverview(overviewEn);
                            }
                        } catch (Exception ex) {
                            // Se falhar o fallback, mantém o filme com dados originais
                            ex.printStackTrace();
                        }
                    }
                    
                    filmes.add(movie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filmes;
    }

    // ---------------------------------------
    // 2) Buscar 1 FILME por ID
    // ---------------------------------------
    public Movie getMovieById(Long id) {
        String url = apiConfig.getBaseUrl()
                + "/movie/" + id
                + "?api_key=" + apiConfig.getApiKey()
                + "&language=pt-BR";

        try {
            String resposta = client.get(url);
            JsonNode node = mapper.readTree(resposta);
            Movie movie = convertJson(node);
            
            // Se não tiver overview em pt-BR, busca em inglês como fallback
            if (movie.getOverview() == null || movie.getOverview().trim().isEmpty()) {
                String urlEn = apiConfig.getBaseUrl()
                        + "/movie/" + id
                        + "?api_key=" + apiConfig.getApiKey()
                        + "&language=en-US";
                
                String respostaEn = client.get(urlEn);
                JsonNode nodeEn = mapper.readTree(respostaEn);
                
                // Usa título e sinopse em inglês se disponíveis
                String titleEn = nodeEn.path("title").asText("");
                String overviewEn = nodeEn.path("overview").asText("");
                
                if (!titleEn.isEmpty()) {
                    movie.setTitle(titleEn);
                }
                if (!overviewEn.isEmpty()) {
                    movie.setOverview(overviewEn);
                }
            }
            
            return movie;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------
    // 3) Converter JSON → Movie
    // ---------------------------------------
    private Movie convertJson(JsonNode node) {
        Movie movie = new Movie();
        movie.setId(node.get("id").asLong());
        movie.setTitle(node.path("title").asText("Sem título"));
        movie.setOverview(node.path("overview").asText(""));
        movie.setPosterPath(node.path("poster_path").isNull() ? null : node.path("poster_path").asText());
        movie.setReleaseDate(node.path("release_date").asText("Data desconhecida"));
        movie.setVoteAverage(node.path("vote_average").asDouble(0.0));
        return movie;
    }
}
