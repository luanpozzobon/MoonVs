package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.SearchPlaylistsCommand;
import lpz.moonvs.application.playlist.output.SearchPlaylistsOutput;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.contracts.model.PlaylistSearchQuery;
import lpz.moonvs.domain.playlist.entity.Playlist;

import java.util.List;

public class SearchPlaylistsUseCase {
    private final IPlaylistRepository repository;

    public SearchPlaylistsUseCase(final IPlaylistRepository repository) {
        this.repository = repository;
    }

    public SearchPlaylistsOutput execute(final SearchPlaylistsCommand command) {
        final var query = new PlaylistSearchQuery(
                command.title()
        );

        final List<Playlist> playlists = this.repository.search(command.userId(), query);

        return SearchPlaylistsOutput.from(playlists);
    }
}
