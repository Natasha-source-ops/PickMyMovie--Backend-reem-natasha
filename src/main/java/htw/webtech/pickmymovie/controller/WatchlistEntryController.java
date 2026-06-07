package htw.webtech.pickmymovie.controller;

import htw.webtech.pickmymovie.model.WatchlistEntry;
import htw.webtech.pickmymovie.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistEntryController {

    @Autowired
    private WatchlistService watchlistService;

    @PostMapping
    public WatchlistEntry addToWatchlist(@RequestBody WatchlistEntry entry) {
        return watchlistService.addToWatchlist(entry);
    }

    @GetMapping("/{userId}")
    public List<WatchlistEntry> getWatchlist(@PathVariable Long userId) {
        return watchlistService.getWatchlist(userId);
    }
}