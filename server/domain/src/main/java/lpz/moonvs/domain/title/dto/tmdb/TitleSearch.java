package lpz.moonvs.domain.title.dto.tmdb;

public record TitleSearch(Integer id,
                          String title,
                          String overview,
                          String posterPath,
                          String mediaType) { }