package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.GetAllPlaylistsCommand;
import lpz.moonvs.application.playlist.output.GetAllPlaylistsOutput;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;

import java.util.List;

public class GetAllPlaylistsUseCase {
    private final IPlaylistRepository repository;

    public GetAllPlaylistsUseCase(final IPlaylistRepository repository) {
        this.repository = repository;
    }

    public GetAllPlaylistsOutput execute(final GetAllPlaylistsCommand command) {
        final List<Playlist> playlists = this.repository.findAll(command.userId());

        return GetAllPlaylistsOutput.from(playlists);
    }
}
