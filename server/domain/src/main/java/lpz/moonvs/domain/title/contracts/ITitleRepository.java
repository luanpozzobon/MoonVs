package lpz.moonvs.domain.title.contracts;

import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.contracts.model.TitleSearchQuery;
import lpz.moonvs.domain.title.entity.Title;

import java.util.List;
import java.util.Optional;

public interface ITitleRepository {
    Title save(final Title title);

    Optional<Title> findById(final Id<Title> id);

    Optional<Title> findByTmdbId(final Integer tmdbId);

    List<Title> search(final TitleSearchQuery query);

    int getTotalPagesFromSearch(final TitleSearchQuery query);
}
