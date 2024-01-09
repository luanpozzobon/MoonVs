package luan.moonvs.controllers;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.enums.SearchType;
import luan.moonvs.models.responses.ContentSearch;
import luan.moonvs.models.requests.ExternalContentRequest;
import luan.moonvs.models.responses.Response;
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

    @Deprecated
    @GetMapping("/internal-search")
    public ResponseEntity<List<ContentSearch>> internalSearch(@RequestParam String title) {
        if (title == null || title.isBlank())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        title = title.replace("%20", " ");
        return service.internalSearch(title);
    }

    @Deprecated
    @GetMapping("/external-search")
    public ResponseEntity<List<ContentSearch>> externalSearch(@RequestParam String title) {
        title = title.replace(" ", "%20");
        if (title == null || title.isBlank())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        return service.externalSearch(title);
    }

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

    @Deprecated
    @GetMapping("internal/{id}")
    public ResponseEntity<Content> viewInternalContent(@PathVariable int id){
        if (id < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        return service.internalContent(id);
    }

    @Deprecated
    @GetMapping("/external")
    public ResponseEntity<?> viewExternalContent(@RequestParam int id, @RequestParam ContentType contentType) {
        if (id < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        return service.externalContent(id, contentType);
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
