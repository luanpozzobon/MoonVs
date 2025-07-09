package lpz.moonvs.domain.title.contracts;

import lpz.moonvs.domain.title.entity.Genre;

public interface IGenreRepository {
    Genre save(final Genre genre);

    Genre findById(final Long id);

    Genre findByTmdbId(final Integer tmdbId);
}
