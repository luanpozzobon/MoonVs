package luan.moonvs.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import luan.moonvs.config.TmdbModule;
import luan.moonvs.models.builders.ContentBuilder;
import luan.moonvs.models.entities.Content;
import luan.moonvs.models.tmdb_responses.TmdbResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class TmdbService {
    @Value("${tmdb.url}")
    private String baseUrl;

    @Value("${tmdb.token}")
    private String token;

    private RestTemplate restTemplate = new RestTemplate();

    private ContentBuilder contentBuilder;

    @Autowired
    public TmdbService(ContentBuilder contentBuilder) {
        this.contentBuilder = contentBuilder;
    }

    public List<Content> search(String title) {
        final String QUERY = "search/multi?query=";
        final String URL = String.format(baseUrl, QUERY, title);

        final HttpHeaders HEADERS = new HttpHeaders() {{
            setContentType(MediaType.APPLICATION_JSON);
            setBearerAuth(token);
        }};
        final HttpEntity ENTITY = new HttpEntity(HEADERS);

        ObjectMapper mapper = TmdbModule.createConfiguredObjectMapper();

        TmdbResults searchResults = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                ENTITY,
                new ParameterizedTypeReference<TmdbResults>() {}
        ).getBody();

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
    }
}