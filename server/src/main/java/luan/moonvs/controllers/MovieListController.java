package luan.moonvs.controllers;

import luan.moonvs.models.entities.MovieList;
import luan.moonvs.models.requests.MovieListRequest;
import luan.moonvs.services.MovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
public class MovieListController {
    @Autowired
    private MovieListService service;

    private final String HEADER_NAME = "message";

    @PostMapping("/create")
    public ResponseEntity<MovieList> createList(@RequestBody MovieListRequest movieListRequest) {
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

    @DeleteMapping("/{listId}/delete")
    public ResponseEntity<?> deleteList(@PathVariable Long listId) {
        final String ID_NOT_PRESENT = "The list id should be provided!";

        if (listId == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, ID_NOT_PRESENT)
                    .build();

        var response = service.delete(listId);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .build();
    }

    @PostMapping("/{idList}/add")
    public ResponseEntity<?> addContent(@PathVariable Long idList, @RequestParam Integer idContent) {
        final String ID_NOT_PRESENT = "The %s id should be provided!";

        if (idList == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, String.format(ID_NOT_PRESENT, "list"))
                    .build();

        if (idContent == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, String.format(ID_NOT_PRESENT, "content"))
                    .build();

        var response = service.add(idList, idContent);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }
}
