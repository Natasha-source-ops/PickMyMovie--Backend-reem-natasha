package htw.webtech.pickmymovie.controller;

import htw.webtech.pickmymovie.model.Rating;
import htw.webtech.pickmymovie.service.RatingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/ratings")
    public Rating saveRating(@RequestBody Rating entry) {
        return ratingService.saveRating(entry);
    }

    @GetMapping("/ratings/movie/{movieId}")
    public List<Rating> getRatingsByMovie(@PathVariable Long movieId) {
        return ratingService.getRatingsByMovie(movieId);
    }

    @GetMapping("/ratings/movie/{movieId}/average")
    public double getAverageRatingByMovie(@PathVariable Long movieId) {
        return ratingService.getAverageRatingByMovie(movieId);
    }

    @GetMapping("/ratings/averages")
    public Map<Long, Double> getAverageRatingsByMovies(
            @RequestParam List<Long> movieIds
    ) {
        return ratingService.getAverageRatingsByMovies(movieIds);
    }

    @DeleteMapping("/ratings/{id}")
    public void deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }
}