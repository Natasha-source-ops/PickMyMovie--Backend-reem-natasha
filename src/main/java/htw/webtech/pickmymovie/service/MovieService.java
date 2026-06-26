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
import java.util.Map;

@Service
public class MovieService {

    private static final String TMDB_DISCOVER_MOVIES_URL =
            "https://api.themoviedb.org/3/discover/movie";

    private static final String TMDB_SEARCH_MOVIES_URL =
            "https://api.themoviedb.org/3/search/movie";

    private static final String TMDB_WATCH_PROVIDERS_URL =
            "https://api.themoviedb.org/3/movie/{movieId}/watch/providers";

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

        URI uri;

        if (hasQuery) {
            uri = UriComponentsBuilder
                    .fromUriString(TMDB_SEARCH_MOVIES_URL)
                    .queryParam("api_key", tmdbApiKey)
                    .queryParam("language", "en-US")
                    .queryParam("query", query.trim())
                    .build()
                    .toUri();
        } else {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(TMDB_DISCOVER_MOVIES_URL)
                    .queryParam("api_key", tmdbApiKey)
                    .queryParam("language", "en-US")
                    .queryParam("sort_by", "popularity.desc");

            if (genreId != null && !genreId.isBlank()) {
                builder.queryParam("with_genres", genreId);
            }

            if (hasProvider) {
                builder.queryParam("watch_region", watchRegion);
                builder.queryParam("with_watch_providers", providerId);
                builder.queryParam("with_watch_monetization_types", "flatrate");
            }

            uri = builder.build().toUri();
        }

        TmdbResponse response = restTemplate.getForObject(uri, TmdbResponse.class);

        if (response == null || response.getResults() == null) {
            return List.of();
        }

        return response.getResults()
                .stream()
                .filter(movie -> !hasProvider || isMovieAvailableOnProvider(movie.getId(), providerId, watchRegion))
                .map(this::toMovieResponse)
                .toList();
    }

    private String normalizeRegion(String region) {
        if (region == null || region.isBlank()) {
            return "DE";
        }

        return region.trim().toUpperCase();
    }

    @SuppressWarnings("unchecked")
    private boolean isMovieAvailableOnProvider(Long movieId, String providerId, String region) {
        if (movieId == null || providerId == null || providerId.isBlank()) {
            return false;
        }

        URI uri = UriComponentsBuilder
                .fromUriString(TMDB_WATCH_PROVIDERS_URL.replace("{movieId}", movieId.toString()))
                .queryParam("api_key", tmdbApiKey)
                .build()
                .toUri();

        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);

        if (response == null || !response.containsKey("results")) {
            return false;
        }

        Map<String, Object> results = (Map<String, Object>) response.get("results");
        Object regionDataObject = results.get(region);

        if (!(regionDataObject instanceof Map)) {
            return false;
        }

        Map<String, Object> regionData = (Map<String, Object>) regionDataObject;
        Object flatrateObject = regionData.get("flatrate");

        if (!(flatrateObject instanceof List)) {
            return false;
        }

        List<Map<String, Object>> providers = (List<Map<String, Object>>) flatrateObject;

        return providers.stream().anyMatch(provider -> {
            Object providerIdObject = provider.get("provider_id");
            return providerIdObject != null && providerIdObject.toString().equals(providerId);
        });
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