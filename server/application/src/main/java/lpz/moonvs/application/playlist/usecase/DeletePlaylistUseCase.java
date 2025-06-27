package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.DeletePlaylistCommand;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import lpz.moonvs.domain.seedwork.valueobject.Id;

public class DeletePlaylistUseCase {
    private final IPlaylistRepository repository;

    public DeletePlaylistUseCase(final IPlaylistRepository repository) {
        this.repository = repository;
    }

    public void execute(final DeletePlaylistCommand command) {
        final Playlist playlist = this.findAndValidatePlaylist(command.id());
        this.validateUserAccess(command.userId(), playlist);

        this.repository.delete(playlist);
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
