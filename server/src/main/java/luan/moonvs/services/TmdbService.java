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
import luan.moonvs.utils.RequestEntity;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.*;

@Service
class TmdbService {
    @Value("${tmdb.url}")
    private String baseUrl;

    @Value("${tmdb.new-url}")
    private String newUrl;

    @Value("${tmdb.token}")
    private String token;

    private HttpHeaders HEADERS;

    private final int MAX_PAGES = 50;

    @PostConstruct
    private void init() {
        this.HEADERS = new HttpHeaders() {{
            setContentType(MediaType.APPLICATION_JSON);
            setBearerAuth(token);
        }};
    }

    @Deprecated
    public List<TmdbSearch> search(String title) {
        final String QUERY = "search/multi?query=";
        final String URL = String.format(baseUrl, QUERY, title);

        final HttpRequestEntity<?, TmdbResults> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS, TmdbResults.class);
        ResponseEntity<TmdbResults> searchResults = request.exchange();

        return searchResults.getBody().results();
    }

    public List<TmdbSearch> search(String title, boolean includeAdult) throws URISyntaxException {
        final String URL = String.format(newUrl, "search/multi");

        final String language = "en-US";
        int page = 1;
        Map<String, String> parameters = new LinkedHashMap<>() {{
            put("query", String.valueOf(title));
            put("include_adult", String.valueOf(includeAdult));
            put("language", String.valueOf(language));
        }};
        List<TmdbSearch> results = new ArrayList<>();

        while(true) {
            parameters.put("page", String.valueOf(page));
            RequestEntity<?, TmdbResults> request = new RequestEntity<>(
                    URL,
                    HttpMethod.GET,
                    HEADERS,
                    TmdbResults.class
            );
            request.addParameters(parameters);
            ResponseEntity<TmdbResults> searchResults = request.exchange();

            if (!searchResults.getStatusCode().is2xxSuccessful()
            ||  !searchResults.hasBody())
                return results;

            TmdbResults response = searchResults.getBody();
            if (response.results() == null)
                return results;

            results.addAll(response.results());

            if (page == response.totalPages() || page++ == MAX_PAGES)
                return results;
        }
    }

    public Content viewMovie(int id) {
        final String QUERY = "movie/";
        final String URL = String.format(baseUrl, QUERY, id);

        HttpRequestEntity<?, TmdbMovie> request = new HttpRequestEntity<>(URL, HttpMethod.GET, HEADERS, TmdbMovie.class);
        ResponseEntity<TmdbMovie> movie = request.exchange();

        if (!movie.getStatusCode().is2xxSuccessful())
            return null;

        if (movie.getBody() == null)
            return null;

        ProviderResults providers = getWatchProviders(QUERY, id);
        ProviderType brProviders = null;
        if (providers != null)
            brProviders = providers.results().get("BR");

        return ContentBuilder.create(movie.getBody())
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

        if (series.getBody() == null)
            return null;

        ProviderResults providers = getWatchProviders(QUERY, id);
        ProviderType brProviders = null;
        if (providers != null)
            brProviders = providers.results().get("BR");

        return ContentBuilder.create(series.getBody())
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