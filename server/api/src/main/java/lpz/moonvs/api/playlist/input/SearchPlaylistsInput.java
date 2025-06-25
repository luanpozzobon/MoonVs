package lpz.moonvs.api.playlist.input;

import io.swagger.v3.oas.annotations.Parameter;

public record SearchPlaylistsInput(
        @Parameter(description = "The playlist's title", example = "Watchlist")
        String title
) {
}
