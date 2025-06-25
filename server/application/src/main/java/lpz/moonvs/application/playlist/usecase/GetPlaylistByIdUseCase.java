package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.GetPlaylistByIdCommand;
import lpz.moonvs.application.playlist.output.GetPlaylistByIdOutput;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;

public class GetPlaylistByIdUseCase {
    private final IPlaylistRepository repository;

    public GetPlaylistByIdUseCase(final IPlaylistRepository repository) {
        this.repository = repository;
    }

    public GetPlaylistByIdOutput execute(final GetPlaylistByIdCommand command) {
        if (command.playlistId().getValue() == null || command.playlistId().getValue().isBlank())
            throw new IllegalArgumentException("Invalid playlist id.");

        final Playlist playlist = this.repository.findById(command.playlistId());
        if (playlist == null)
            throw new PlaylistNotFoundException("There is no playlist with the given id.");

        if (!command.userId().equals(playlist.getUserId()))
            throw new NoAccessToResourceException("The authenticated user, has no access to this playlist.");

        return GetPlaylistByIdOutput.from(playlist);
    }
}
