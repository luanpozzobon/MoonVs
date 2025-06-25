package lpz.moonvs.domain.title.contracts;

import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;

public interface ITitleRepository {
    Title save(final Title title);

    Title findById(final Id<Title> id);

    Title findByTmdbId(final Long tmdbId);
}
