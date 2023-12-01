package luan.moonvs.models.responses;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;

public record ContentSearchDTO(int tmdbId, String originalTitle, String overview, ContentType contentType, double voteAverage) {
    public ContentSearchDTO(Content content) {
        this(content.getIdTmdb(), content.getOriginalTitle(), content.getOverview(), content.getContentType(), content.getTmdbVoteAvg());
    }
}
