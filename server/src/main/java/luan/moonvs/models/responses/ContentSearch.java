package luan.moonvs.models.responses;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.tmdb_responses.TmdbSearch;

public record ContentSearch(int id,
                            String originalTitle,
                            String overview,
                            ContentType contentType,
                            double voteAverage) {
    public ContentSearch(Content content) {
        this(content.getIdContent(),
             content.getOriginalTitle(),
             content.getOverview(),
             content.getContentType(),
             content.getTmdbVoteAvg());
    }

    public ContentSearch(TmdbSearch tmdbSearch) {
        this(tmdbSearch.id(),
             tmdbSearch.originalTitle(),
             tmdbSearch.overview(),
             tmdbSearch.contentType(),
             tmdbSearch.voteAvg());
    }
}
