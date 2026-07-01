package htw.webtech.pickmymovie.service;

import htw.webtech.pickmymovie.controller.dto.MovieResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "tmdb.api.key=")
class MovieServiceIntegrationTest {

    @Autowired
    private MovieService movieService;

    @Test
    void getAllMoviesReturnsFallbackMoviesWhenTmdbApiKeyIsMissing() {
        List<MovieResponse> movies = movieService.getAllMovies(null, null, null, null);;

        assertThat(movies)
                .hasSize(3)
                .extracting(MovieResponse::title)
                .containsExactly("Inception", "The Notebook", "The Dark Knight");

        MovieResponse firstMovie = movies.getFirst();
        assertThat(firstMovie.id()).isEqualTo(1L);
        assertThat(firstMovie.overview()).isEqualTo("A thriller");
        assertThat(firstMovie.releaseDate()).isEqualTo("2010-07-16");
        assertThat(firstMovie.posterPath()).isEqualTo("/inception.jpg");
    }
}
