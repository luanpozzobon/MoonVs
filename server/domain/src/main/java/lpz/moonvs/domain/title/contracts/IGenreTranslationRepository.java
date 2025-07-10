package lpz.moonvs.domain.title.contracts;

import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Genre;
import lpz.moonvs.domain.title.entity.GenreTranslation;

import java.util.List;

public interface IGenreTranslationRepository {
    GenreTranslation save(final GenreTranslation genreTranslation);

    List<GenreTranslation> findAllById(final Id<Genre> id);

    GenreTranslation findByIdAndLanguage(final Id<Genre> id,
                                         final String language);
}
