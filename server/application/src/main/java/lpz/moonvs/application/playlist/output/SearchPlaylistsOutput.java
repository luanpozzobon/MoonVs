package lpz.moonvs.application.playlist.output;

import java.util.ArrayList;
import java.util.List;

public record SearchPlaylistsOutput(List<Playlist> playlists) {
    public static SearchPlaylistsOutput from(final List<lpz.moonvs.domain.playlist.entity.Playlist> playlists) {
        final var output = new SearchPlaylistsOutput(new ArrayList<>());
        playlists.forEach(playlist ->
                output.playlists.add(new Playlist(playlist.getId().getValue(), playlist.getTitle(), playlist.getDescription()))
        );

        return output;
    }

    public record Playlist(String id,
                           String title,
                           String description) { }
}
