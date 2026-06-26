package htw.webtech.pickmymovie.controller.dto;

public record RegionResponse(
        String iso31661,
        String englishName,
        String nativeName
) {
}