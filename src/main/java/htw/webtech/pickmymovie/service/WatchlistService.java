package htw.webtech.pickmymovie.service;

import htw.webtech.pickmymovie.Repository.WatchListEntryRepository;
import htw.webtech.pickmymovie.model.WatchlistEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class WatchlistService {

    @Autowired
    private WatchListEntryRepository watchListEntryRepository;

    public WatchlistEntry addToWatchlist(WatchlistEntry entry) {
        entry.setAddedDate(LocalDate.now(ZoneId.of("Europe/Berlin")));
        return watchListEntryRepository.save(entry);
    }

    public void removeFromWatchlist(long userId, long movieId) {
        watchListEntryRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    public List<WatchlistEntry> getWatchlist(long userId) {
        return watchListEntryRepository.findByUserId(userId);
    }

    public void deleteWatchlistEntry(Long id) {
        watchListEntryRepository.deleteById(id);
    }

    public void deleteWatchlistEntriesByUserId(Long userId) {
        watchListEntryRepository.deleteByUserId(userId);
    }
}