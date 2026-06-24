package htw.webtech.pickmymovie.controller;

import htw.webtech.pickmymovie.controller.dto.MovieResponse;
import htw.webtech.pickmymovie.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieResponse>> getMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String query
    ) {
        return ResponseEntity.ok(movieService.getAllMovies(genre, query));
    }
}