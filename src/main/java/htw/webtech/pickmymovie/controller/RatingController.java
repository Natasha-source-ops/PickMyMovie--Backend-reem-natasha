package htw.webtech.pickmymovie.controller;
import htw.webtech.pickmymovie.model.Rating;
import htw.webtech.pickmymovie.service.RatingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<Rating> getRating(@PathVariable Long movieId) {
        return ratingService.getRatingsByMovie(movieId);
    }

    @DeleteMapping("/ratings/{id}")
    public void deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }
}

