package htw.webtech.pickmymovie.service;

import htw.webtech.pickmymovie.Repository.RatingRepository;
import htw.webtech.pickmymovie.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }
}


