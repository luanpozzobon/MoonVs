package luan.moonvs.services;

import jakarta.annotation.PostConstruct;
import luan.moonvs.models.builders.ContentBuilder;
import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.tmdb_responses.TmdbContent;
import luan.moonvs.models.tmdb_responses.TmdbResults;
import luan.moonvs.models.tmdb_responses.TmdbSearch;
import luan.moonvs.models.tmdb_responses.providers.ProviderResults;
import luan.moonvs.models.tmdb_responses.providers.ProviderType;
import luan.moonvs.utils.RequestEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
class TmdbService {
    @Value("${tmdb.new-url}")
    private String newUrl;

    @Value("${tmdb.token}")
    private String token;

    private HttpHeaders HEADERS;

    private final int MAX_PAGES = 50;
    private final String API_VERSION = "3";

    @PostConstruct
    private void init() {
        this.HEADERS = new HttpHeaders() {{
            setContentType(MediaType.APPLICATION_JSON);
            setBearerAuth(token);
        }};
    }

    public List<TmdbSearch> search(String title, boolean includeAdult) throws URISyntaxException {
        final String type = "multi";
        final String language = "en-US";
        int page = 1;
        Map<String, String> parameters = new LinkedHashMap<>() {{
            put("query", String.valueOf(title));
            put("include_adult", String.valueOf(includeAdult));
            put("language", String.valueOf(language));
        }};
        List<TmdbSearch> results = new ArrayList<>();

        while (true) {
            parameters.put("page", String.valueOf(page));
            var request = new RequestEntity<>(newUrl, HttpMethod.GET, HEADERS, TmdbResults.class);
            var searchResults = request
                    .setPath(API_VERSION, "search", type)
                    .addParameters(parameters)
                    .exchange();

            if (!searchResults.getStatusCode().is2xxSuccessful()
            ||  !searchResults.hasBody())
                return results;

            TmdbResults response = searchResults.getBody();
            if (response == null || response.results() == null)
                return results;

            results.addAll(response.results());

            if (page == response.totalPages() || page++ == MAX_PAGES)
                return results;
        }
    }

    public Content getContent(int id, ContentType contentType) throws URISyntaxException {
        final String language = "en-US";

        final var request = new RequestEntity<>(newUrl, HttpMethod.GET, HEADERS, TmdbContent.class);
        final var content = request
                .setPath(API_VERSION, contentType.getContentType(), String.valueOf(id))
                .addParameter("language", language)
                .exchange();

        if (!content.getStatusCode().is2xxSuccessful()
        ||  !content.hasBody()
        ||   content.getBody() == null)
            return null;

        ProviderResults providers = getWatchProviders(contentType, id);
        ProviderType brProviders = null;
        if (providers != null)
            brProviders = providers.results().get("BR");

        return ContentBuilder
                .create(content.getBody(), contentType)
                .withProviders(brProviders)
                .build();
    }

    private ProviderResults getWatchProviders(final ContentType contentType, int id) throws URISyntaxException {
        final var request = new RequestEntity<>(newUrl, HttpMethod.GET, HEADERS, ProviderResults.class);
        final var providers = request
                .setPath(API_VERSION, contentType.getContentType(), String.valueOf(id), "watch", "providers")
                .exchange();

        if (!providers.getStatusCode().is2xxSuccessful()
        || !providers.hasBody())
            return null;

        return providers.getBody();
    }
}