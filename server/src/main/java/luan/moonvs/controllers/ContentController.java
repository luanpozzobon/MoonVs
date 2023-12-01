package luan.moonvs.controllers;

import luan.moonvs.models.responses.ContentSearchDTO;
import luan.moonvs.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContentController {

    @Autowired
    private ContentService service;

    @GetMapping("/internal-search/{title}")
    public ResponseEntity<List<ContentSearchDTO>> internalSearch(@PathVariable String title) {
        return service.internalSearch(title);
    }

    @GetMapping("/external-search/{title}")
    public ResponseEntity<List<ContentSearchDTO>> externalSearch(@PathVariable String title) {
        return service.externalSearch(title);
    }
}
