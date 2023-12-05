package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.entities.Content;
import luan.moonvs.models.responses.ContentSearch;
import luan.moonvs.models.requests.ExternalContentRequest;
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
    private ContentRepository repository;
    private TmdbService tmdbService;

    @Autowired
    public ContentService(ContentRepository repository, TmdbService tmdbService) {
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
        if (id == 0)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

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

    public ResponseEntity<?> externalContent(ExternalContentRequest contentView) {
        if (contentView.id() == 0)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        switch (contentView.contentType()){
            case MOVIE -> {
                var movie = tmdbService.viewMovie(contentView.id());
                if (movie == null)
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .build();

                repository.save(movie);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(movie);
            }
            case TV -> {
                var series = tmdbService.viewSeries(contentView.id());
                if (series == null)
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .build();

                repository.save(series);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(series);
            }
            default -> {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No such Content Type exists");
            }
        }
    }
}
