package htw.webtech.pickmymovie.controller.dto;

public record RatingResponse(
        Long id,
        Long userId,
        String username,
        Long movieId,
        int score,
        String comment
) {
}