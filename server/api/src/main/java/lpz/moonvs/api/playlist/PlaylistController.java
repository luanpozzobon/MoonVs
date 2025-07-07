package lpz.moonvs.api.playlist;

import lpz.moonvs.api.playlist.input.AddTitleToPlaylistInput;
import lpz.moonvs.api.playlist.input.CreatePlaylistInput;
import lpz.moonvs.api.playlist.input.SearchPlaylistsInput;
import lpz.moonvs.api.playlist.input.UpdatePlaylistInput;
import lpz.moonvs.application.playlist.command.*;
import lpz.moonvs.application.playlist.output.*;
import lpz.moonvs.application.playlist.usecase.*;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.config.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/playlists")
public class PlaylistController implements IPlaylistController {
    private final CreatePlaylistUseCase createPlaylistUseCase;
    private final GetAllPlaylistsUseCase getAllPlaylistsUseCase;
    private final GetPlaylistByIdUseCase getPlaylistByIdUseCase;
    private final SearchPlaylistsUseCase searchPlaylistsUseCase;
    private final UpdatePlaylistUseCase updatePlaylistUseCase;
    private final DeletePlaylistUseCase deletePlaylistUseCase;
    private final AddTitleToPlaylistUseCase addTitleToPlaylistUseCase;
    private final RemoveTitleFromPlaylistUseCase removeTitleFromPlaylistUseCase;
    private final GetAllTitlesFromPlaylistUseCase getAllTitlesFromPlaylistUseCase;

    @Autowired
    public PlaylistController(final CreatePlaylistUseCase createPlaylistUseCase,
                              final GetAllPlaylistsUseCase getAllPlaylistsUseCase,
                              final GetPlaylistByIdUseCase getPlaylistByIdUseCase,
                              final SearchPlaylistsUseCase searchPlaylistsUseCase,
                              final UpdatePlaylistUseCase updatePlaylistUseCase,
                              final DeletePlaylistUseCase deletePlaylistUseCase,
                              final AddTitleToPlaylistUseCase addTitleToPlaylistUseCase,
                              final RemoveTitleFromPlaylistUseCase removeTitleFromPlaylistUseCase,
                              final GetAllTitlesFromPlaylistUseCase getAllTitlesFromPlaylistUseCase) {
        this.createPlaylistUseCase = createPlaylistUseCase;
        this.getAllPlaylistsUseCase = getAllPlaylistsUseCase;
        this.getPlaylistByIdUseCase = getPlaylistByIdUseCase;
        this.searchPlaylistsUseCase = searchPlaylistsUseCase;
        this.updatePlaylistUseCase = updatePlaylistUseCase;
        this.deletePlaylistUseCase = deletePlaylistUseCase;
        this.addTitleToPlaylistUseCase = addTitleToPlaylistUseCase;
        this.removeTitleFromPlaylistUseCase = removeTitleFromPlaylistUseCase;
        this.getAllTitlesFromPlaylistUseCase = getAllTitlesFromPlaylistUseCase;
    }

    @Override
    @PostMapping(path = "/")
    public ResponseEntity<CreatePlaylistOutput> create(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                       @RequestBody final CreatePlaylistInput input) {
        final CreatePlaylistOutput output = this.createPlaylistUseCase.execute(
                new CreatePlaylistCommand(userDetails.getId(), input.title(), input.description())
        );

        final URI uri = URI.create("/playlists/" + output.id());

        return ResponseEntity
                .created(uri)
                .body(output);
    }

    @Override
    @GetMapping(path = "/")
    public ResponseEntity<GetAllPlaylistsOutput> getAll(@AuthenticationPrincipal final CustomUserDetails userDetails) {
        final GetAllPlaylistsOutput output = this.getAllPlaylistsUseCase.execute(
                new GetAllPlaylistsCommand(userDetails.getId())
        );

        return ResponseEntity
                .ok()
                .body(output);
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<GetPlaylistByIdOutput> getById(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                         @PathVariable("id") final String id) {
        final GetPlaylistByIdOutput output = this.getPlaylistByIdUseCase.execute(
                new GetPlaylistByIdCommand(userDetails.getId(), Id.from(id))
        );

        return ResponseEntity
                .ok()
                .body(output);
    }

    @Override
    @GetMapping(path = "/search")
    public ResponseEntity<SearchPlaylistsOutput> getByTitle(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                            final SearchPlaylistsInput input) {
        final SearchPlaylistsOutput output = this.searchPlaylistsUseCase.execute(
                new SearchPlaylistsCommand(userDetails.getId(), input.title())
        );

        return ResponseEntity
                .ok()
                .body(output);
    }

    @Override
    @PutMapping(path = "/{id}")
    public ResponseEntity<UpdatePlaylistOutput> update(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                       @PathVariable("id") final String id,
                                                       @RequestBody final UpdatePlaylistInput input) {
        final UpdatePlaylistOutput output = this.updatePlaylistUseCase.execute(
                new UpdatePlaylistCommand(userDetails.getId(), Id.from(id), input.title(), input.description())
        );

        return ResponseEntity
                .ok()
                .body(output);
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                    @PathVariable("id") final String id) {
        this.deletePlaylistUseCase.execute(
                new DeletePlaylistCommand(userDetails.getId(), Id.from(id))
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    @PostMapping(path = "/{id}/items/")
    public ResponseEntity<?> add(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                 @PathVariable("id") final String id,
                                 @RequestBody final AddTitleToPlaylistInput input) {
        this.addTitleToPlaylistUseCase.execute(
                new AddTitleToPlaylistCommand(userDetails.getId(), Id.from(id), Id.from(input.titleId()), input.type())
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    @DeleteMapping(path = "/{id}/items/{titleId}")
    public ResponseEntity<?> remove(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                    @PathVariable("id") final String id,
                                    @PathVariable("titleId") final Long titleId) {
        this.removeTitleFromPlaylistUseCase.execute(
                new RemoveTitleFromPlaylistCommand(userDetails.getId(), Id.from(id), Id.from(titleId))
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    @GetMapping(path = "/{id}/items/")
    public ResponseEntity<GetAllTitlesFromPlaylistOutput> getAllTitles(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                                       @PathVariable("id") final String id,
                                                                       @RequestParam("page") final Integer page) {
        final GetAllTitlesFromPlaylistOutput output = this.getAllTitlesFromPlaylistUseCase.execute(
                new GetAllTitlesFromPlaylistCommand(userDetails.getId(), Id.from(id), page)
        );

        return ResponseEntity
                .ok()
                .body(output);
    }
}
