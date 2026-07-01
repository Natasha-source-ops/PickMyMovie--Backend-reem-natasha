package htw.webtech.pickmymovie.Controller;

import htw.webtech.pickmymovie.controller.RatingController;
import htw.webtech.pickmymovie.service.RatingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RatingController.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RatingService ratingService;

    @Test
    @DisplayName("GET /api/v1/ratings/movie/{movieId}/average - should return average rating")
    void testGetAverageRatingByMovie() throws Exception {
        when(ratingService.getAverageRatingByMovie(99L)).thenReturn(3.0);

        mockMvc.perform(get("/api/v1/ratings/movie/99/average"))
                .andExpect(status().isOk())
                .andExpect(content().string("3.0"));
    }
}


