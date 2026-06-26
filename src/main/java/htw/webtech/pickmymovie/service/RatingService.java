package htw.webtech.pickmymovie.service;

import htw.webtech.pickmymovie.Repository.RatingRepository;
import htw.webtech.pickmymovie.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsByMovie(Long movieId) {
        return ratingRepository.findByMovieId(movieId);
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

    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    public Map<Long, Double> getAverageRatingsByMovies(List<Long> movieIds) {
        return movieIds.stream()
                .distinct()
                .collect(Collectors.toMap(
                        movieId -> movieId,
                        this::getAverageRatingByMovie
                ));
    }
}

