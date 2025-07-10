package lpz.moonvs.domain.title.dto.tmdb;

import java.util.List;

public record Translations(Integer id,
                           List<Translation> translations) {

    public record Translation(String iso3166,
                              String iso639,
                              Data data) { }

    public record Data(String overview,
                       String tagline,
                       String title){ }
}
