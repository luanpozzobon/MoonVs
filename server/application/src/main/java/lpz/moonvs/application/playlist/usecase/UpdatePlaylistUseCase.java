package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.UpdatePlaylistCommand;
import lpz.moonvs.application.playlist.output.UpdatePlaylistOutput;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistAlreadyExistsException;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;

import java.util.List;

public class UpdatePlaylistUseCase {
    public static final String ALREADY_EXISTS_ERROR_KEY = "error.common.already-exists";

    private final IPlaylistRepository repository;

    public UpdatePlaylistUseCase(final IPlaylistRepository repository) {
        this.repository = repository;
    }

    public UpdatePlaylistOutput execute(final UpdatePlaylistCommand command) {
        final Playlist playlist = this.findAndValidatePlaylist(command.id());
        this.validateUserAccess(command.userId(), playlist);

        final NotificationHandler handler = NotificationHandler.create();
        this.validateAndRenamePlaylist(command.userId(), command.title(), playlist, handler);
        this.validateAndUpdateDescription(command.description(), playlist, handler);

        this.repository.update(playlist);

        return UpdatePlaylistOutput.from(playlist);
    }

    private Playlist findAndValidatePlaylist(final Id<Playlist> playlistId) {
        return this.repository.findById(playlistId)
                .orElseThrow(PlaylistNotFoundException::new);
    }

    private void validateUserAccess(final Id<User> userId,
                                    final Playlist playlist) {
        if (!userId.equals(playlist.getUserId()))
            throw new NoAccessToResourceException();
    }

    private void validateAndRenamePlaylist(final Id<User> userId,
                                           final String title,
                                           final Playlist playlist,
                                           final NotificationHandler handler) {
        if (title == null) return;

        if (playlist.getTitle().equals(title)) return;

        if (!this.repository.findByTitle(userId, title).isEmpty())
            throw new PlaylistAlreadyExistsException(List.of(
                    new Notification(Playlist.TITLE_KEY, ALREADY_EXISTS_ERROR_KEY, Playlist.RESOURCE_KEY, Playlist.TITLE_KEY, title)));

        playlist.rename(handler, title);
    }

    private void validateAndUpdateDescription(final String description,
                                              final Playlist playlist,
                                              final NotificationHandler handler) {
        if (description == null) return;

        if (playlist.getDescription().equals(description)) return;

        playlist.updateDescription(handler, description);
    }
}
