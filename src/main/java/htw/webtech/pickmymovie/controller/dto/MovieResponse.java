package htw.webtech.pickmymovie.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieResponse(
        Long id,
        String title,
        @JsonProperty("overview")
        String overview,
        @JsonProperty("release_date")
        String releaseDate,
        @JsonProperty("poster_path")
        String posterPath
) {
}
