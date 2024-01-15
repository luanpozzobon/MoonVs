package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.enums.SearchType;
import luan.moonvs.models.responses.ContentSearch;
import luan.moonvs.models.responses.Response;
import luan.moonvs.models.tmdb_responses.TmdbSearch;
import luan.moonvs.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ContentService {
    private final ContentRepository repository;
    private final TmdbService tmdbService;
    private final AccountService accountService;

    @Autowired
    private ContentService(ContentRepository repository,
                           TmdbService tmdbService,
                           AccountService accountService) {
        this.repository = repository;
        this.tmdbService = tmdbService;
        this.accountService = accountService;
    }

    @Deprecated
    public ResponseEntity<List<ContentSearch>> internalSearch(String title) {
        List<Content> contents = repository.getByOriginalTitleContaining(title);
        if (contents.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

        List<ContentSearch> response = new ArrayList<>(){{
            contents.forEach(content ->
                    add(new ContentSearch(content))
            );
        }};

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @Deprecated
    public ResponseEntity<List<ContentSearch>> externalSearch(String title) {
        List<TmdbSearch> contents = tmdbService.search(title);
        if (contents.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

        List<ContentSearch> response = new ArrayList<>(){{
            contents.forEach(content -> {
                if (content != null)
                    add(new ContentSearch(content));
            });
        }};

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    public Response<List<ContentSearch>> search(SearchType searchType, String title) {
        final String BAD_SEARCH = "There's no such search type!",
                     NOT_EXISTING = "The title you've searched, doesn't exist!";

        List<ContentSearch> response;

        if (searchType == null)
            searchType = SearchType.INTERNAL;

        title = URLDecoder.decode(title, StandardCharsets.UTF_8);

        switch (searchType) {
            case INTERNAL:
                response = databaseSearch(title);

                break;
            case EXTERNAL:
                try {
                    response = tmdbSearch(title);
                } catch (URISyntaxException e) {
                    return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, new ArrayList<>());
                }

                break;
            default:
                return new Response<>(HttpStatus.BAD_REQUEST, BAD_SEARCH);
        }

        if (response == null)
            return new Response<>(HttpStatus.NOT_FOUND, NOT_EXISTING);

        return new Response<>(HttpStatus.OK, response);
    }

    private List<ContentSearch> databaseSearch(String title) {
        List<Content> contents = repository.getByOriginalTitleContaining(title);
        if (contents.isEmpty())
            return null;

        return contents.stream()
                .filter(Objects::nonNull)
                .map(ContentSearch::new)
                .collect(Collectors.toList());
    }

    private List<ContentSearch> tmdbSearch(String title) throws URISyntaxException {
        boolean includeAdult = accountService.getAccount().isOfLegalAge();

        List<TmdbSearch> contents = tmdbService.search(title, includeAdult);
        if (contents.isEmpty())
            return null;

        return contents.stream()
                .filter(Objects::nonNull)
                .map(ContentSearch::new)
                .collect(Collectors.toList());
    }

    @Deprecated
    public ResponseEntity<Content> internalContent(int id) {
        try {
            Content content = repository.getReferenceById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(content);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @Deprecated
    public ResponseEntity<Content> externalContent(int id, ContentType contentType) {
        if (repository.existsByIdTmdb(id))
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(repository.getReferenceByIdTmdb(id));

        Content content = null;

        switch (contentType){
            case MOVIE -> content = tmdbService.viewMovie(id);
            case TV -> content = tmdbService.viewSeries(id);
        }

        if (content == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

        repository.save(content);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(content);
    }

    public Response<Content> viewContent(int id, SearchType searchType, ContentType contentType) {
        final String BAD_SEARCH = "There's no such search type!",
                     NOT_EXISTING = "The requested content, couldn't be found!";

        if (searchType == null)
            searchType = SearchType.INTERNAL;

        Content content;
        switch (searchType) {
            case INTERNAL:
                content = viewDatabaseContent(id);

                break;
            case EXTERNAL:
                try {
                    content = viewTmdbContent(id, contentType);
                } catch (URISyntaxException e) {
                    return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, new Content());
                }

                break;
            default:
                return new Response<>(HttpStatus.BAD_REQUEST, BAD_SEARCH);
        }

        if (content == null)
            return new Response<>(HttpStatus.NOT_FOUND, NOT_EXISTING);

        return new Response<>(HttpStatus.OK, content);
    }

    private Content viewDatabaseContent(int id) {
        try {
            return repository.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    private Content viewTmdbContent(int id, ContentType contentType) throws URISyntaxException {
        if (repository.existsByIdTmdb(id))
            return repository.getReferenceByIdTmdb(id);

        Content content = null;

        content = tmdbService.getContent(id, contentType);

        if (content != null)
            repository.save(content);

        return content;
    }
}
