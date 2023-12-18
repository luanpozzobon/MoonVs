package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.entities.Content;
import luan.moonvs.models.requests.ExternalContentRequest;
import luan.moonvs.models.responses.ContentSearch;
import luan.moonvs.models.tmdb_responses.TmdbSearch;
import luan.moonvs.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService {
    private final ContentRepository repository;
    private final TmdbService tmdbService;

    @Autowired
    private ContentService(ContentRepository repository, TmdbService tmdbService) {
        this.repository = repository;
        this.tmdbService = tmdbService;
    }

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

    public ResponseEntity<Content> externalContent(ExternalContentRequest contentView) {
        if (repository.existsByIdTmdb(contentView.id()))
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(repository.getReferenceByIdTmdb(contentView.id()));

        Content content = null;

        switch (contentView.contentType()){
            case MOVIE -> content = tmdbService.viewMovie(contentView.id());
            case TV -> content = tmdbService.viewSeries(contentView.id());
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
}
