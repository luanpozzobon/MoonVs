package luan.moonvs.services;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.responses.ContentSearchDTO;
import luan.moonvs.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService {
    private ContentRepository contentRepository;
    private TmdbService tmdbService;

    @Autowired
    public ContentService(ContentRepository contentRepository, TmdbService tmdbService) {
        this.contentRepository = contentRepository;
        this.tmdbService = tmdbService;
    }

    public ResponseEntity<List<ContentSearchDTO>> internalSearch(String title) {
        List<Content> contents = contentRepository.findByOriginalTitle(title);
        if (contents.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

        List<ContentSearchDTO> response = new ArrayList<>(){{
            contents.forEach(content ->
                    add(new ContentSearchDTO(content))
            );
        }};

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    public ResponseEntity<List<ContentSearchDTO>> externalSearch(String title) {
        List<Content> contents = tmdbService.search(title);
        if (contents.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();

        List<ContentSearchDTO> response = new ArrayList<>(){{
            contents.forEach(content ->
                    add(new ContentSearchDTO(content))
            );
        }};

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
