package htw.webtech.pickmymovie.service;
import htw.webtech.pickmymovie.Repository.RatingRepository;
import htw.webtech.pickmymovie.model.Rating;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class RatingServiceTest {

    @Autowired
    private RatingService service;

    @MockitoBean
    private RatingRepository repository;

    @Test
    @DisplayName("should calculate average rating for a movie")
    void testGetAverageRatingByMovie() {
        Rating r1 = new Rating(1L, 10L, 99L, 4, "Good");
        Rating r2 = new Rating(2L, 11L, 99L, 2, "Meh");

        doReturn(List.of(r1, r2)).when(repository).findByMovieId(99L);

        double average = service.getAverageRatingByMovie(99L);

        assertEquals(3.0, average);
    }
}
