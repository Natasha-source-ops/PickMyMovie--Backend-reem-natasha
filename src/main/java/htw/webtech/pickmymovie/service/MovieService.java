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

    public List<MovieResponse> getAllMovies(String genreId, String query, String providerId, String region) {
        if (tmdbApiKey == null || tmdbApiKey.isBlank()) {
            return getFallbackMovies();
        }

        boolean hasQuery = query != null && !query.isBlank();
        boolean hasProvider = providerId != null && !providerId.isBlank();
        String watchRegion = normalizeRegion(region);

        UriComponentsBuilder builder;

        if (hasQuery && !hasProvider) {
            builder = UriComponentsBuilder
                    .fromUriString(TMDB_SEARCH_MOVIES_URL)
                    .queryParam("api_key", tmdbApiKey)
                    .queryParam("language", "en-US")
                    .queryParam("query", query.trim());
        } else {
            builder = UriComponentsBuilder
                    .fromUriString(TMDB_DISCOVER_MOVIES_URL)
                    .queryParam("api_key", tmdbApiKey)
                    .queryParam("language", "en-US")
                    .queryParam("sort_by", "popularity.desc");

            if (hasProvider) {
                builder.queryParam("watch_region", watchRegion);
                builder.queryParam("with_watch_providers", providerId);
                builder.queryParam("with_watch_monetization_types", "flatrate");
            }
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
                .filter(movie -> !hasProvider || matchesSearchQuery(movie, query))
                .map(this::toMovieResponse)
                .toList();
    }

    private String normalizeRegion(String region) {
        if (region == null || region.isBlank()) {
            return "DE";
        }

        return region.trim().toUpperCase();
    }

    private boolean matchesSearchQuery(TmdbMovie movie, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }

        if (movie.getTitle() == null) {
            return false;
        }

        return movie.getTitle().toLowerCase().contains(query.trim().toLowerCase());
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