package luan.moonvs.services;

import jakarta.annotation.PostConstruct;
import luan.moonvs.models.builders.ContentBuilder;
import luan.moonvs.models.entities.Content;
import luan.moonvs.models.tmdb_responses.TmdbMovie;
import luan.moonvs.models.tmdb_responses.TmdbResults;
import luan.moonvs.models.tmdb_responses.TmdbSearch;
import luan.moonvs.models.tmdb_responses.TmdbTv;
import luan.moonvs.models.tmdb_responses.providers.ProviderResults;
import luan.moonvs.models.tmdb_responses.providers.ProviderType;
import luan.moonvs.utils.HttpRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TmdbService {
    @Value("${tmdb.url}")
    private String baseUrl;

    @Value("${tmdb.token}")
    private String token;

    private HttpHeaders HEADERS;

    private final ContentBuilder contentBuilder;

    @Autowired
    private TmdbService(ContentBuilder contentBuilder) {
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

        final HttpRequestEntity<?, TmdbResults> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS, TmdbResults.class);
        ResponseEntity<TmdbResults> searchResults = request.exchange();

        return searchResults.getBody().results();
    }

    public Content viewMovie(int id) {
        final String QUERY = "movie/";
        final String URL = String.format(baseUrl, QUERY, id);

        HttpRequestEntity<?, TmdbMovie> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS, TmdbMovie.class);
        ResponseEntity<TmdbMovie> movie = request.exchange();

        if (!movie.getStatusCode().is2xxSuccessful())
            return null;

        ProviderResults providers = getWatchProviders(QUERY, id);
        ProviderType brProviders = null;
        if (providers != null)
            brProviders = providers.results().get("BR");

        return contentBuilder
                .fromTmdbMovie(movie.getBody())
                .withProviders(brProviders)
                .build();
    }

    public Content viewSeries(int id) {
        final String QUERY = "tv/";
        final String URL = String.format(baseUrl, QUERY, id);

        HttpRequestEntity<?, TmdbTv> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS, TmdbTv.class);
        ResponseEntity<TmdbTv> series = request.exchange();

        if (!series.getStatusCode().is2xxSuccessful())
            return null;

        ProviderResults providers = getWatchProviders(QUERY, id);
        ProviderType brProviders = null;
        if (providers != null)
            brProviders = providers.results().get("BR");

        return contentBuilder
                .fromTmdbTv(series.getBody())
                .withProviders(brProviders)
                .build();
    }

    private ProviderResults getWatchProviders(final String QUERY, int id) {
        final String URL = String.format(baseUrl, QUERY, id + "/watch/providers");

        final HttpRequestEntity<?, ProviderResults> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS, ProviderResults.class);
        ResponseEntity<ProviderResults> providers = request.exchange();

        if (!providers.getStatusCode().is2xxSuccessful())
            return null;

        return providers.getBody();
    }
}