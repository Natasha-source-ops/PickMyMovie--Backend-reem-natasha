package htw.webtech.pickmymovie.service;

import htw.webtech.pickmymovie.controller.dto.MovieResponse;
import htw.webtech.pickmymovie.model.TmdbMovie;
import htw.webtech.pickmymovie.model.TmdbResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class MovieService {

    private static final String TMDB_DISCOVER_MOVIES_URL =
            "https://api.themoviedb.org/3/discover/movie";

    private static final String TMDB_SEARCH_MOVIES_URL =
            "https://api.themoviedb.org/3/search/movie";

    private final RestTemplate restTemplate;
    private final String tmdbApiKey;

    public MovieService(RestTemplate restTemplate, @Value("${tmdb.api.key:}") String tmdbApiKey) {
        this.restTemplate = restTemplate;
        this.tmdbApiKey = tmdbApiKey;
    }

    public List<MovieResponse> getAllMovies(String genreId, String query) {
        if (tmdbApiKey == null || tmdbApiKey.isBlank()) {
            return getFallbackMovies();
        }

        UriComponentsBuilder builder;

        if (query != null && !query.isBlank()) {
            builder = UriComponentsBuilder
                    .fromUriString(TMDB_SEARCH_MOVIES_URL)
                    .queryParam("api_key", tmdbApiKey)
                    .queryParam("language", "en-US")
                    .queryParam("query", query);
        } else {
            builder = UriComponentsBuilder
                    .fromUriString(TMDB_DISCOVER_MOVIES_URL)
                    .queryParam("api_key", tmdbApiKey)
                    .queryParam("language", "en-US")
                    .queryParam("sort_by", "popularity.desc");
        }

        if (genreId != null && !genreId.isBlank()) {
            builder.queryParam("with_genres", genreId);
        }

        URI uri = builder.build().toUri();

        TmdbResponse response = restTemplate.getForObject(uri, TmdbResponse.class);

        if (response == null || response.getResults() == null) {
            return List.of();
        }

        return response.getResults()
                .stream()
                .map(this::toMovieResponse)
                .toList();
    }

    private List<MovieResponse> getFallbackMovies() {
        return List.of(
                new MovieResponse(1L, "Inception", "A thriller", "2010-07-16", "/inception.jpg"),
                new MovieResponse(2L, "The Notebook", "Romance", "2004-06-25", "/notebook.jpg"),
                new MovieResponse(3L, "The Dark Knight", "Action", "2008-07-18", "/darkknight.jpg")
        );
    }

    private MovieResponse toMovieResponse(TmdbMovie tmdbMovie) {
        return new MovieResponse(
                tmdbMovie.getId(),
                tmdbMovie.getTitle(),
                tmdbMovie.getDescription(),
                tmdbMovie.getReleaseDate(),
                tmdbMovie.getImageUrl()
        );
    }
}