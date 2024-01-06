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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ContentBuilder {
    private Content content;

    public static ContentBuilder create() {
        ContentBuilder contentBuilder = new ContentBuilder();
        contentBuilder.content = new Content();

        return contentBuilder;
    }

    public static ContentBuilder create(TmdbMovie tmdbMovie) {
        ContentBuilder contentBuilder = new ContentBuilder();
        contentBuilder.content = new Content();

        contentBuilder.content.setIdTmdb(tmdbMovie.id());
        contentBuilder.content.setAdult(tmdbMovie.adult());
        contentBuilder.content.setOriginalTitle(tmdbMovie.originalTitle());
        contentBuilder.content.setPtTitle(tmdbMovie.ptTitle());
        contentBuilder.content.setOverview(tmdbMovie.overview());
        contentBuilder.content.setGenres(tmdbMovie.genres().stream()
                .map(TmdbGenres::genre)
                .collect(Collectors.toList()));
        contentBuilder.content.setPosterPath(tmdbMovie.posterPath());
        contentBuilder.content.setTmdbVoteAvg(tmdbMovie.voteAvg());
        contentBuilder.content.setTmdbVoteCount(tmdbMovie.voteCount());
        contentBuilder.content.setContentType(ContentType.MOVIE);

        return contentBuilder;
    }

    public static ContentBuilder create(TmdbTv tmdbTv) {
        ContentBuilder contentBuilder = new ContentBuilder();
        contentBuilder.content = new Content();

        contentBuilder.content.setIdTmdb(tmdbTv.id());
        contentBuilder.content.setAdult(tmdbTv.adult());
        contentBuilder.content.setOriginalTitle(tmdbTv.originalTitle());
        contentBuilder.content.setPtTitle(tmdbTv.ptTitle());
        contentBuilder.content.setOverview(tmdbTv.overview());
        contentBuilder.content.setGenres(tmdbTv.genres().stream()
                .map(TmdbGenres::genre)
                .collect(Collectors.toList()));
        contentBuilder.content.setPosterPath(tmdbTv.posterPath());
        contentBuilder.content.setTmdbVoteAvg(tmdbTv.voteAvg());
        contentBuilder.content.setTmdbVoteCount(tmdbTv.voteCount());
        contentBuilder.content.setContentType(ContentType.MOVIE);

        return contentBuilder;
    }

    public ContentBuilder withProviders(ProviderType providerType) {
        if (providerType == null)
            return this;

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
