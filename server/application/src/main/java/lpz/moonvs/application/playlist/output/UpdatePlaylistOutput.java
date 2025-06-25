package lpz.moonvs.application.playlist.output;

import lpz.moonvs.domain.playlist.entity.Playlist;

public record UpdatePlaylistOutput(String id,
                                   String title,
                                   String description) {
    public static UpdatePlaylistOutput from(final Playlist playlist) {
        return new UpdatePlaylistOutput(playlist.getId().getValue(), playlist.getTitle(), playlist.getDescription());
    }
}
