package luan.moonvs.controllers;

import luan.moonvs.models.requests.MovieListRequest;
import luan.moonvs.services.MovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lists")
public class MovieListController {
    @Autowired
    private MovieListService service;

    private final String HEADER_NAME = "message";

    @PostMapping("/create")
    public ResponseEntity<?> createList(@RequestBody MovieListRequest movieListRequest) {
        final String NAME_NOT_PRESENT = "You should provide a name for the list!";

        if (movieListRequest.listName() == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, NAME_NOT_PRESENT)
                    .build();

        var response = service.create(movieListRequest);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }

    
}
