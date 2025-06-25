package lpz.moonvs.api.playlist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lpz.moonvs.api.playlist.input.AddTitleToPlaylistInput;
import lpz.moonvs.api.playlist.input.CreatePlaylistInput;
import lpz.moonvs.api.playlist.input.SearchPlaylistsInput;
import lpz.moonvs.api.playlist.input.UpdatePlaylistInput;
import lpz.moonvs.application.playlist.output.*;
import lpz.moonvs.infra.config.security.CustomUserDetails;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Playlists")
public interface IPlaylistController {
    @Operation(
            summary = "Creates a new playlist!",
            description = "Validates the info, and creates a new playlist.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Playlist created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CreatePlaylistOutput.class),
                                    examples = {@ExampleObject(name = "Playlist created", externalValue = "/openapi/playlists/create.success.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid info",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Invalid info", externalValue = "/openapi/playlists/create.bad-request.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Playlist already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Existing playlist", externalValue = "openapi/playlists/create.conflict.json")}
                            )
                    )
            }
    )
    @PostMapping(path = "/")
    ResponseEntity<CreatePlaylistOutput> create(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreatePlaylistInput.class),
                            examples = {@ExampleObject(name = "New playlist", externalValue = "/openapi/playlists/create.request.json")}
                    )
            )
            @RequestBody final CreatePlaylistInput input
    );

    @Operation(
            summary = "Get all your playlists",
            description = "Gets a list of all the user's playlist.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GetAllPlaylistsOutput.class),
                                    examples = {
                                            @ExampleObject(name = "Playlists", externalValue = "/openapi/playlists/get-all.success.json"),
                                            @ExampleObject(name = "Empty", externalValue = "/openapi/playlists/get-all.empty.json")
                                    }
                            )
                    )
            }
    )
    @GetMapping(path = "/")
    ResponseEntity<GetAllPlaylistsOutput> getAll(@AuthenticationPrincipal final CustomUserDetails userDetails);

    @Operation(
            summary = "Get a specific playlist",
            description = "Finds a specific playlist by its given id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GetPlaylistByIdOutput.class),
                                    examples = {@ExampleObject(name = "Playlist", externalValue = "/openapi/playlists/get-by-id.success.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid playlist Id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Invalid Id", externalValue = "/openapi/playlists/get-by-id.bad-request.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "The authenticated user has no access to the playlist identified by the given Id.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Forbidden", externalValue = "/openapi/playlists/get-by-id.forbidden.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Playlist not found.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Playlist not found", externalValue = "openapi/playlists/get-by-id.not-found.json")}
                            )
                    )
            }
    )
    @GetMapping(path = "/{id}")
    ResponseEntity<GetPlaylistByIdOutput> getById(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(
                    description = "Unique Id, identifies the playlist to be returned",
                    required = true,
                    example = "a4727bb1-58e2-46bb-8c4a-25d7568c4f0a"
            )
            @PathVariable("id") final String id
    );

    @Operation(
            summary = "Search the playlists",
            description = "Search the user's playlists, using multiple parameters",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SearchPlaylistsOutput.class),
                                    examples = {
                                            @ExampleObject(name = "Playlists", externalValue = "/openapi/playlists/search.success.json"),
                                            @ExampleObject(name = "Empty", externalValue = "/openapi/playlists/search.empty.json")
                                    }
                            )
                    )
            }
    )
    @GetMapping(path = "/search")
    ResponseEntity<SearchPlaylistsOutput> getByTitle(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @ParameterObject final SearchPlaylistsInput input
    );

    @Operation(
            summary = "Updates a playlist",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UpdatePlaylistOutput.class),
                                    examples = {@ExampleObject(name = "Playlist", externalValue = "/openapi/playlists/update.success.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Invalid Id or info", externalValue = "/openapi/playlists/update.bad-request.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Playlist not found", externalValue = "/openapi/playlists/update.not-found.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "There is a playlist with this title already", externalValue = "/openapi/playlists/update.conflict.json")}
                            )
                    )
            }
    )
    @PutMapping(path = "/{id}")
    ResponseEntity<UpdatePlaylistOutput> update(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(
                    description = "Unique Id, identifies the playlist",
                    required = true,
                    example = "a4727bb1-58e2-46bb-8c4a-25d7568c4f0a"
            )
            @PathVariable("id") final String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdatePlaylistInput.class),
                            examples = {@ExampleObject(name = "Update playlist", externalValue = "/openapi/playlists/update.request.json")}
                    )
            )
            @RequestBody final UpdatePlaylistInput input
    );

    @Operation(
            summary = "Deletes the playlist",
            responses = {
                    @ApiResponse(
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Invalid Id", externalValue = "/openapi/playlists/delete.bad-request.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Playlist not found", externalValue = "/openapi/playlists/delete.not-found.json")}
                            )
                    )
            }
    )
    @DeleteMapping(path = "/{id}")
    ResponseEntity<?> delete(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(
                    description = "Unique Id, identifies the playlist",
                    required = true,
                    example = "a4727bb1-58e2-46bb-8c4a-25d7568c4f0a"
            )
            @PathVariable("id") final String id
    );

    @Operation(
            summary = "Adds a title to a playlist",
            description = "The field 'titleId' receives the TMDB id of the content, you can get this id by searching the title in TMDB and copying the number in URL. The field 'type' accepts the values: 'TV' | 'MOVIE'",
            responses = {
                    @ApiResponse(
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Invalid Id", externalValue = "/openapi/playlists/add.bad-request.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Playlist not found", externalValue = "/openapi/playlists/add.not-found.json")}
                            )
                    )
            }
    )
    @PostMapping(path = "/{id}/items/")
    ResponseEntity<?> add(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(
                    description = "Unique Id, identifies the playlist",
                    required = true,
                    example = "a4727bb1-58e2-46bb-8c4a-25d7568c4f0a"
            )
            @PathVariable("id") final String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AddTitleToPlaylistInput.class),
                            examples = {@ExampleObject(name = "Add title to playlist", externalValue = "/openapi/playlists/add.request.json")}
                    )
            )
            @RequestBody final AddTitleToPlaylistInput input
    );

    @Operation(
            summary = "Removes a title from a playlist",
            responses = {
                    @ApiResponse(
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Invalid Id", externalValue = "/openapi/playlists/remove.bad-request.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {
                                            @ExampleObject(name = "Playlist does not exist", externalValue = "/openapi/playlists/remove.playlist-not-found.json"),
                                            @ExampleObject(name = "Title does not exist", externalValue = "/openapi/playlists/remove.title-not-found.json")
                                    }
                            )
                    )
            }
    )
    @DeleteMapping(path = "/{id}/items/{titleId}")
    ResponseEntity<?> remove(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(
                    description = "Unique Id, identifies the playlist",
                    required = true,
                    example = "a4727bb1-58e2-46bb-8c4a-25d7568c4f0a"
            )
            @PathVariable("id") final String id,
            @Parameter(
                    description = "Unique Id, identifies the title",
                    required = true,
                    example = "1412"
            )
            @PathVariable("titleId") final Long titleId
    );

    @Operation(
            summary = "Returns the titles present in a playlist.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GetAllTitlesFromPlaylistOutput.class),
                                    examples = {@ExampleObject(name = "Titles", externalValue = "/openapi/playlists/get-titles.success.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Invalid Id", externalValue = "/openapi/playlists/get-titles.bad-request.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Playlist not found")}
                            )
                    )
            }
    )
    @GetMapping(path = "/{id}/items")
    ResponseEntity<GetAllTitlesFromPlaylistOutput> getAllTitles(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @Parameter(
                    description = "Unique Id, identifies the playlist",
                    required = true,
                    example = "a4727bb1-58e2-46bb-8c4a-25d7568c4f0a"
            )
            @PathVariable("id") final String id,
            @Parameter(
                    description = "Number of the page, used to load big lists",
                    required = true,
                    example = "1"
            )
            @RequestParam("page") final Integer page
    );
}
