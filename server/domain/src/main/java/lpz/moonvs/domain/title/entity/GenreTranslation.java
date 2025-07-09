package lpz.moonvs.domain.title.entity;

import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public class GenreTranslation {
    private final Id<Genre> genreId;
    private final Id<Language> languageId;
    private final String name;

    private GenreTranslation(final Id<Genre> genreId,
                             final Id<Language> languageId,
                             final String name) {
        this.genreId = genreId;
        this.languageId = languageId;
        this.name = name;
    }

    public static GenreTranslation create(final Id<Language> languageId,
                                          final String name) {
        return new GenreTranslation(null, languageId, name);
    }

    public static GenreTranslation load(final Id<Genre> genreId,
                                        final Id<Language> languageId,
                                        final String name) {
        return new GenreTranslation(genreId, languageId, name);
    }
}
