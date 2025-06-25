package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.UpdatePlaylistCommand;
import lpz.moonvs.application.playlist.output.UpdatePlaylistOutput;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistAlreadyExistsException;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;

import java.util.List;

public class UpdatePlaylistUseCase {
    private static final String EXISTING_PLAYLIST = "There is already a playlist created with this title.";

    private final IPlaylistRepository repository;

    public UpdatePlaylistUseCase(final IPlaylistRepository repository) {
        this.repository = repository;
    }

    public UpdatePlaylistOutput execute(final UpdatePlaylistCommand command) {
        if (command.id().getValue() == null || command.id().getValue().isBlank())
            throw new IllegalArgumentException("Invalid playlist id.");

        final Playlist playlist = this.repository.findById(command.id());
        if (playlist == null || !command.userId().equals(playlist.getUserId()))
            throw new PlaylistNotFoundException("There is no playlist with the given id.");

        final NotificationHandler handler = NotificationHandler.create();
        if (command.title() != null) {
            if (!this.repository.findByTitle(command.userId(), command.title()).isEmpty())
                throw new PlaylistAlreadyExistsException(EXISTING_PLAYLIST, List.of(new Notification("title", command.title())));

            playlist.rename(handler, command.title());
        }
        if (command.description() != null) {
            playlist.updateDescription(handler, command.description());
        }

        this.repository.update(playlist);

        return UpdatePlaylistOutput.from(playlist);
    }
}
