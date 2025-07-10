package lpz.moonvs.domain.title.contracts;

import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.title.entity.GenreTranslation;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.domain.title.entity.TitleTranslation;
import lpz.moonvs.domain.title.entity.Type;

import java.util.List;
import java.util.Optional;

public interface ITMDbSearchService {
    List<Title> search(final String title,
                       final Boolean includeAdult,
                       final Language language,
                       final Integer page);

    Optional<Title> findDetailedTitle(final Integer tmdbId,
                               final Type type,
                               final Language language);

    List<TitleTranslation> findTitleTranslations(final Integer tmdbId,
                                                 final Type type);

    List<GenreTranslation> findGenreTranslations(final Integer tmdbId,
                                                 final Language language);

    String findPoster(final Integer tmdbId,
                      final Type type,
                      final Language language);
}
