package luan.moonvs.models.builders;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.tmdb_responses.TmdbGenres;
import luan.moonvs.models.tmdb_responses.TmdbMovie;
import luan.moonvs.models.tmdb_responses.TmdbTv;
import luan.moonvs.models.tmdb_responses.providers.Provider;
import luan.moonvs.models.tmdb_responses.providers.ProviderType;
import luan.moonvs.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ContentBuilder {
    private Content content;

    public ContentBuilder fromTmdbMovie(TmdbMovie tmdbMovie) {
        this.content = new Content();

        this.content.setIdTmdb(tmdbMovie.id());
        this.content.setAdult(tmdbMovie.adult());
        this.content.setOriginalTitle(tmdbMovie.originalTitle());
        this.content.setPtTitle(tmdbMovie.ptTitle());
        this.content.setOverview(tmdbMovie.overview());
        this.content.setGenres(tmdbMovie.genres().stream()
                .map(TmdbGenres::genre)
                .collect(Collectors.toList()));
        this.content.setTmdbVoteAvg(tmdbMovie.voteAvg());
        this.content.setTmdbVoteCount(tmdbMovie.voteCount());
        this.content.setContentType(ContentType.MOVIE);
        return this;
    }

    public ContentBuilder fromTmdbTv(TmdbTv tmdbTv) {
        this.content = new Content();

        this.content.setIdTmdb(tmdbTv.id());
        this.content.setAdult(tmdbTv.adult());
        this.content.setOriginalTitle(tmdbTv.originalTitle());
        this.content.setPtTitle(tmdbTv.ptTitle());
        this.content.setOverview(tmdbTv.overview());
        this.content.setGenres(tmdbTv.genres().stream()
                .map(TmdbGenres::genre)
                .collect(Collectors.toList()));
        this.content.setTmdbVoteAvg(tmdbTv.voteAvg());
        this.content.setTmdbVoteCount(tmdbTv.voteCount());
        this.content.setContentType(ContentType.MOVIE);
        return this;
    }

    public ContentBuilder withProviders(ProviderType providerType) {
        List<String> flatrate = providerType.flatrate() != null ?
                providerType.flatrate().stream()
                        .map(Provider::providerName)
                        .toList() :
                Collections.emptyList();
        List<String> buy = providerType.buy() != null ?
                providerType.buy().stream()
                        .map(Provider::providerName)
                        .toList() :
                Collections.emptyList();
        List<String> rent = providerType.rent() != null ?
                providerType.rent().stream()
                        .map(Provider::providerName)
                        .toList() :
                Collections.emptyList();

        this.content.setWatchProvider(
                Map.of(
                    "flatrate", flatrate,
                    "buy", buy,
                    "rent", rent
                )
        );
        return this;
    }


    public Content build() {
        return this.content;
    }
}
