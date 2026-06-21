package htw.webtech.pickmymovie.Repository;

import htw.webtech.pickmymovie.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovieId(Long movieId);

}
