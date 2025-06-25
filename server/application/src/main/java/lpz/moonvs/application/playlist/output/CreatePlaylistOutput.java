package lpz.moonvs.application.playlist.output;

import lpz.moonvs.domain.playlist.entity.Playlist;

public record CreatePlaylistOutput(String id,
                                   String title,
                                   String description) {
    public static CreatePlaylistOutput from(final Playlist playlist) {
        return new CreatePlaylistOutput(playlist.getId().getValue(), playlist.getTitle(), playlist.getDescription());
    }
}
