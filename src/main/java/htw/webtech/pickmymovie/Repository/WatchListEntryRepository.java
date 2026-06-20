package htw.webtech.pickmymovie.Repository;

import htw.webtech.pickmymovie.model.WatchlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListEntryRepository extends JpaRepository<WatchlistEntry, Long> {

    void deleteByUserIdAndMovieId(long userId, long movieId);

    void deleteByUserId(Long userId);

    List<WatchlistEntry> findByUserId(long userId);
}