package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.RemoveTitleFromPlaylistCommand;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.playlist.exception.PlaylistItemNotFoundException;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;

public class RemoveTitleFromPlaylistUseCase {
    private final IPlaylistItemRepository repository;
    private final IPlaylistRepository playlistRepository;

    public RemoveTitleFromPlaylistUseCase(final IPlaylistItemRepository repository,
                                          final IPlaylistRepository playlistRepository) {
        this.repository = repository;
        this.playlistRepository = playlistRepository;
    }

    public void execute(final RemoveTitleFromPlaylistCommand command) {
        if (command.playlistId().getValue() == null || command.playlistId().getValue().isBlank())
            throw new IllegalArgumentException("Invalid playlist id.");

        final Playlist playlist = this.playlistRepository.findById(command.playlistId());
        if (playlist == null || !command.userId().equals(playlist.getUserId()))
            throw new PlaylistNotFoundException("There is no playlist with the given id.");

        final PlaylistItem playlistItem = this.repository.findByPlaylistIdAndTitleId(command.playlistId(), command.titleId());
        if (playlistItem == null)
            throw new PlaylistItemNotFoundException("This title is not in the playlist.");

        this.repository.delete(playlistItem);
    }
}
