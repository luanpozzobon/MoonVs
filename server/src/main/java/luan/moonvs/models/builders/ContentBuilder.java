package luan.moonvs.models.builders;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.tmdb_responses.TmdbSearch;
import luan.moonvs.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentBuilder {
    private Content content;
    private ContentRepository contentRepository;

    @Autowired
    public ContentBuilder(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public ContentBuilder fromTmdbSearch(TmdbSearch tmdbSearch) {
        this.content = new Content();
        this.content.setIdTmdb(tmdbSearch.id());
        this.content.setOriginalTitle(tmdbSearch.originalTitle());
        this.content.setOverview(tmdbSearch.overview());
        this.content.setContentType(tmdbSearch.contentType());
        this.content.setTmdbVoteAvg(tmdbSearch.voteAverage());
        return this;
    }


    public Content build() {
        return this.content;
    }
}
