package luan.moonvs.services;

import jakarta.annotation.PostConstruct;
import luan.moonvs.models.builders.ContentBuilder;
import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.tmdb_responses.TmdbMovie;
import luan.moonvs.models.tmdb_responses.TmdbResults;
import luan.moonvs.models.tmdb_responses.TmdbSearch;
import luan.moonvs.models.tmdb_responses.TmdbTv;
import luan.moonvs.models.tmdb_responses.providers.Provider;
import luan.moonvs.models.tmdb_responses.providers.ProviderResults;
import luan.moonvs.utils.HttpRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TmdbService {
    @Value("${tmdb.url}")
    private String baseUrl;

    @Value("${tmdb.token}")
    private String token;

    private HttpHeaders HEADERS;

    private ContentBuilder contentBuilder;

    @Autowired
    public TmdbService(ContentBuilder contentBuilder) {
        this.contentBuilder = contentBuilder;
    }

    @PostConstruct
    private void init() {
        this.HEADERS = new HttpHeaders() {{
            setContentType(MediaType.APPLICATION_JSON);
            setBearerAuth(token);
        }};
    }

    public List<TmdbSearch> search(String title) {
        final String QUERY = "search/multi?query=";
        final String URL = String.format(baseUrl, QUERY, title);

        final HttpRequestEntity<TmdbResults> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS);
        ResponseEntity<TmdbResults> searchResults = request.exchange();


        return searchResults.getBody().results();

        /*
        List<Content> searchedContents = new ArrayList<>();
        searchResults.results().forEach( content -> {
            if (content != null) {
                searchedContents.add(contentBuilder
                        .fromTmdbSearch(content)
                        .build()
                );
            }
        });

        return searchedContents;
        */
    }

    public Content viewMovie(int id) {
        final String QUERY = "movie/";
        final String URL = String.format(baseUrl, QUERY, id);

        HttpRequestEntity<TmdbMovie> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS);
        ResponseEntity<TmdbMovie> movie = request.exchange();

        if (!movie.getStatusCode().is2xxSuccessful())
            return null;

        ProviderResults providers = getWatchProviders(QUERY, id);
        var brProviders = providers.results().get("BR");

        return contentBuilder
                .fromTmdbMovie(movie.getBody())
                .withProviders(brProviders)
                .build();
    }

    public Content viewSeries(int id) {
        final String QUERY = "tv/";
        final String URL = String.format(baseUrl, QUERY, id);

        HttpRequestEntity<TmdbTv> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS);
        ResponseEntity<TmdbTv> series = request.exchange();

        if (!series.getStatusCode().is2xxSuccessful())
            return null;

        ProviderResults providers = getWatchProviders(QUERY, id);
        var brProviders = providers.results().get("BR");

        return contentBuilder
                .fromTmdbTv(series.getBody())
                .withProviders(brProviders)
                .build();
    }

    private ProviderResults getWatchProviders(final String QUERY, int id) {
        final String URL = String.format(baseUrl, QUERY, id + "/watch/providers");

        final HttpRequestEntity<ProviderResults> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS);
        ResponseEntity<ProviderResults> providers = request.exchange();

        if (!providers.getStatusCode().is2xxSuccessful())
            return null;

        return providers.getBody();
    }
}