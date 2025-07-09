package lpz.moonvs.domain.title.dto.tmdb;

import java.time.LocalDate;
import java.util.List;

public record TitleDetails(Integer id,
                           String originalTitle,
                           String mediaType,
                           Integer languageId,
                           List<Genre> genres,
                           Translation translation,
                           LocalDate releaseDate,
                           Boolean adult,
                           Integer screenTime) {

    public record Genre(Integer id, String name) { }

    public record Translation(String title, String tagline, String overview, String posterPath) { }
}
