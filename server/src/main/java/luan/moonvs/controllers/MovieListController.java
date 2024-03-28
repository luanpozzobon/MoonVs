package luan.moonvs.controllers;

import luan.moonvs.models.entities.MovieList;
import luan.moonvs.models.entities.MovieListContent;
import luan.moonvs.models.requests.MovieListRequest;
import luan.moonvs.models.responses.MovieListResponse;
import luan.moonvs.services.MovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lists")
public class MovieListController {
    @Autowired
    private MovieListService service;

    private final String HEADER_NAME = "message";
    private final String ID_NOT_PRESENT = "The %s id should be provided!";

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

    @GetMapping("/get")
    public ResponseEntity<List<MovieListResponse>> getUserLists(@RequestParam UUID idUser) {
        if (idUser == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, String.format(ID_NOT_PRESENT, "user"))
                    .build();

        var response = service.getUserLists(idUser);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }

    @PatchMapping("/{idList}/edit")
    public ResponseEntity<MovieListResponse> edit(@PathVariable Long idList,
                                                  @RequestParam String listName,
                                                  @RequestParam String listDescription) {
        if (idList == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, String.format(ID_NOT_PRESENT, "list"))
                    .build();

        var response = service.edit(idList, listName, listDescription);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }


    @DeleteMapping("/{idList}/delete")
    public ResponseEntity<Void> deleteList(@PathVariable Long idList) {
        if (idList == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, String.format(ID_NOT_PRESENT, "list"))
                    .build();

        var response = service.delete(idList);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .build();
    }

    @GetMapping("/{idList}/get")
    public ResponseEntity<MovieList> getList(@PathVariable Long idList) {
        if (idList == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, String.format(ID_NOT_PRESENT, "list"))
                    .build();

        var response = service.getList(idList);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }

    @PostMapping("/{idList}/add")   
    public ResponseEntity<?> addContent(@PathVariable Long idList, @RequestParam Integer idContent) {
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

    @GetMapping("/{idList}/get-contents")
    public ResponseEntity<List<MovieListContent>> getListContent(@PathVariable Long idList) {
        if (idList == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, String.format(ID_NOT_PRESENT, "list"))
                    .build();

        var response = service.getListContent(idList);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }

    @DeleteMapping("{idList}/remove")
    public ResponseEntity<Void> removeListContent(@PathVariable Long idList, @RequestParam Integer idContent) {
        if (idContent == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, String.format(ID_NOT_PRESENT, "content"))
                    .build();

        var response = service.removeListContent(idList, idContent);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .build();
    }
}