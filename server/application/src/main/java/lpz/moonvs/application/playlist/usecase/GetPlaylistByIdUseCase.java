package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.GetPlaylistByIdCommand;
import lpz.moonvs.application.playlist.output.GetPlaylistByIdOutput;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public class GetPlaylistByIdUseCase {
    private final IPlaylistRepository repository;

    public GetPlaylistByIdUseCase(final IPlaylistRepository repository) {
        this.repository = repository;
    }

    public GetPlaylistByIdOutput execute(final GetPlaylistByIdCommand command) {
        final Playlist playlist = this.findAndValidatePlaylist(command.playlistId());
        this.validateUserAccess(command.userId(), playlist);

        return GetPlaylistByIdOutput.from(playlist);
    }

    private Playlist findAndValidatePlaylist(final Id<Playlist> playlistId) {
        return this.repository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("There is no playlist with the given id."));
    }

    private void validateUserAccess(final Id<User> userId,
                                    final Playlist playlist) {
        if (!userId.equals(playlist.getUserId()))
            throw new NoAccessToResourceException("The authenticated user doesn't have access to this playlist.");
    }
}
