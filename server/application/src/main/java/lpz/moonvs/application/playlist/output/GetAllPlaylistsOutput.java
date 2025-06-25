package lpz.moonvs.application.playlist.output;

import java.util.ArrayList;
import java.util.List;

public record GetAllPlaylistsOutput(List<Playlist> playlists) {
    public static GetAllPlaylistsOutput from(final List<lpz.moonvs.domain.playlist.entity.Playlist> playlists) {
        final var output = new GetAllPlaylistsOutput(new ArrayList<>());
        playlists.forEach(playlist -> output.playlists.add(
                new Playlist(playlist.getId().getValue(), playlist.getTitle(), playlist.getDescription()))
        );

        return output;
    }

    public record Playlist(String id,
                           String title,
                           String description) { }
}
