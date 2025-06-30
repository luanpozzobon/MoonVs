package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.CreatePlaylistCommand;
import lpz.moonvs.application.playlist.output.CreatePlaylistOutput;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistAlreadyExistsException;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;

public class CreatePlaylistUseCase {
    private static final String EXISTING_PLAYLIST = "There is already a playlist created with this title.";

    private final IPlaylistRepository repository;

    public CreatePlaylistUseCase(final IPlaylistRepository repository) {
        this.repository = repository;
    }

    public CreatePlaylistOutput execute(final CreatePlaylistCommand command) throws PlaylistAlreadyExistsException, DomainValidationException {
        final NotificationHandler handler = NotificationHandler.create();
        this.validateIfExists(handler, command);

        final Playlist playlist = Playlist.create(handler, command.userId(), command.title(), command.description());

        return CreatePlaylistOutput.from(this.repository.save(playlist));
    }

    private void validateIfExists(final NotificationHandler handler,
                                  final CreatePlaylistCommand command) throws PlaylistAlreadyExistsException {
        if (command.title() != null && !this.repository.findByTitle(command.userId(), command.title()).isEmpty())
            handler.addError(new Notification(
                    "title", command.title()
            ));

        if (handler.hasError())
            throw new PlaylistAlreadyExistsException(EXISTING_PLAYLIST, handler.getErrors());
    }
}
