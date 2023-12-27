package luan.moonvs.controllers;

import luan.moonvs.models.entities.Content;
import luan.moonvs.models.enums.ContentType;
import luan.moonvs.models.responses.ContentSearch;
import luan.moonvs.models.requests.ExternalContentRequest;
import luan.moonvs.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService service;

    @GetMapping("/internal-search")
    public ResponseEntity<List<ContentSearch>> internalSearch(@RequestParam String title) {
        if (title == null || title.isBlank())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        title = title.replace("%20", " ");
        return service.internalSearch(title);
    }

    @GetMapping("/external-search")
    public ResponseEntity<List<ContentSearch>> externalSearch(@RequestParam String title) {
        title = title.replace(" ", "%20");
        if (title == null || title.isBlank())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        return service.externalSearch(title);
    }

    @GetMapping("internal/{id}")
    public ResponseEntity<Content> viewInternalContent(@PathVariable int id){
        if (id < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        return service.internalContent(id);
    }

    @GetMapping("/external")
    public ResponseEntity<?> viewExternalContent(@RequestParam int id, @RequestParam ContentType contentType) {
        if (id < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        return service.externalContent(id, contentType);
    }
}
