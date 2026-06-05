package htw.webtech.pickmymovie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "watchlist_entries")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WatchlistEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long movieId;

    @Column
    private String movieTitle;

    @Column
    private String posterUrl;

    @Column
    private LocalDate addedDate;
}