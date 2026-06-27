package htw.webtech.pickmymovie.service;

import htw.webtech.pickmymovie.Repository.RatingRepository;
import htw.webtech.pickmymovie.Repository.UserRepository;
import htw.webtech.pickmymovie.controller.dto.MovieResponse;
import htw.webtech.pickmymovie.controller.dto.RatingResponse;
import htw.webtech.pickmymovie.model.Rating;
import htw.webtech.pickmymovie.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieService movieService;

    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public List<RatingResponse> getRatingsByMovie(Long movieId) {
        return ratingRepository.findByMovieId(movieId)
                .stream()
                .map(this::toRatingResponse)
                .toList();
    }

    public List<RatingResponse> getRatingsByUser(Long userId) {
        return ratingRepository.findByUserId(userId)
                .stream()
                .map(this::toRatingResponse)
                .toList();
    }

    public double getAverageRatingByMovie(Long movieId) {
        List<Rating> ratings = ratingRepository.findByMovieId(movieId);

        if (ratings.isEmpty()) {
            return 0.0;
        }

        return ratings.stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }

    public Map<Long, Double> getAverageRatingsByMovies(List<Long> movieIds) {
        return movieIds.stream()
                .distinct()
                .collect(Collectors.toMap(
                        movieId -> movieId,
                        this::getAverageRatingByMovie
                ));
    }

    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    private RatingResponse toRatingResponse(Rating rating) {
        User user = userRepository
                .findById(rating.getUserId())
                .orElse(null);

        String username = user != null ? user.getUsername() : "Unknown User";

        MovieResponse movie = movieService.getMovieById(rating.getMovieId());

        String movieTitle = movie != null ? movie.title() : "Unknown Movie";

        String posterUrl = movie != null && movie.posterPath() != null
                ? "https://image.tmdb.org/t/p/w500" + movie.posterPath()
                : "";

        return new RatingResponse(
                rating.getId(),
                rating.getUserId(),
                username,
                rating.getMovieId(),
                movieTitle,
                posterUrl,
                rating.getScore(),
                rating.getComment()
        );
    }
}