package luan.moonvs.controllers;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.enums.SearchType;
import luan.moonvs.models.responses.ContentSearch;
import luan.moonvs.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService service;

    private final String HEADER_NAME = "message";

    @GetMapping("/search")
    public ResponseEntity<List<ContentSearch>> search(@RequestParam SearchType searchType, @RequestParam String title) {
        final String EMPTY_SEARCH = "Please enter a content name!";

        if (title == null || title.isBlank())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, EMPTY_SEARCH)
                    .build();

        var response = service.search(searchType, title);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Content> viewContent(@PathVariable int id, @RequestParam SearchType searchType, @Nullable @RequestParam ContentType contentType) {
        final String ZERO_OR_NEGATIVE_ID = "The requested content is out of range!";
        if (id < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, ZERO_OR_NEGATIVE_ID)
                    .build();

        var response = service.viewContent(id, searchType, contentType);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }
}