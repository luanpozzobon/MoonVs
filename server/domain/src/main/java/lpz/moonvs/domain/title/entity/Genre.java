package lpz.moonvs.domain.title.entity;

import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.dto.tmdb.TitleDetails;

import java.util.ArrayList;
import java.util.List;

public class Genre {
    private final Id<Genre> id;
    private final Integer tmdbId;
    private List<GenreTranslation> translations;

    private Genre(final Id<Genre> id,
                  final Integer tmdbId,
                  final List<GenreTranslation> translations) {
        this.id = id;
        this.tmdbId = tmdbId;

        if (translations == null)
            this.translations = new ArrayList<>();
        else
            this.translations = translations;
    }

    public static Genre fromDetails(final Integer languageId,
                                    final TitleDetails.Genre genre) {
        List<GenreTranslation> translations = List.of(GenreTranslation.create(Id.from(languageId), genre.name()));

        return new Genre(null, genre.id(), translations);
    }

    public static Genre load(final Id<Genre> id,
                             final Integer tmdbId) {
        return new Genre(id, tmdbId, null);
    }

    public void setTranslations(final List<GenreTranslation> translations) {
        this.translations = translations;
    }

    public void addTranslation(final GenreTranslation translation) {
        this.translations.add(translation);
    }
}
