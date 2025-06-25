package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.DeletePlaylistCommand;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;

public class DeletePlaylistUseCase {
    private final IPlaylistRepository repository;

    public DeletePlaylistUseCase(final IPlaylistRepository repository) {
        this.repository = repository;
    }

    public void execute(final DeletePlaylistCommand command) {
        if (command.id().getValue() == null || command.id().getValue().isBlank())
            throw new IllegalArgumentException("Invalid playlist id.");

        final Playlist playlist = this.repository.findById(command.id());
        if (playlist == null || !command.userId().equals(playlist.getUserId()))
            throw new PlaylistNotFoundException("There is no playlist with the given id.");

        this.repository.delete(playlist);
    }
}
