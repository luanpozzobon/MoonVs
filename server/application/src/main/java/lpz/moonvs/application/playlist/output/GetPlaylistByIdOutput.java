package lpz.moonvs.application.playlist.output;

import lpz.moonvs.domain.playlist.entity.Playlist;

public record GetPlaylistByIdOutput(String id,
                                    String title,
                                    String description) {
    public static GetPlaylistByIdOutput from(final Playlist playlist) {
        return new GetPlaylistByIdOutput(playlist.getId().getValue(), playlist.getTitle(), playlist.getDescription());
    }
}
