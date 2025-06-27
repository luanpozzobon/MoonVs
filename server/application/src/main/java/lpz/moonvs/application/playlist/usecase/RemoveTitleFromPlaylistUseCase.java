package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.RemoveTitleFromPlaylistCommand;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.playlist.exception.PlaylistItemNotFoundException;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;

public class RemoveTitleFromPlaylistUseCase {
    private final IPlaylistItemRepository repository;
    private final IPlaylistRepository playlistRepository;

    public RemoveTitleFromPlaylistUseCase(final IPlaylistItemRepository repository,
                                          final IPlaylistRepository playlistRepository) {
        this.repository = repository;
        this.playlistRepository = playlistRepository;
    }

    public void execute(final RemoveTitleFromPlaylistCommand command) {
        final Playlist playlist = this.findAndValidatePlaylist(command.playlistId());
        this.validateUserAccess(command.userId(), playlist);

        final PlaylistItem playlistItem = this.findAndValidatePlaylistItem(command.playlistId(), command.titleId());

        this.repository.delete(playlistItem);
    }

    private Playlist findAndValidatePlaylist(final Id<Playlist> playlistId) {
        return this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("There is no playlist with the given id."));
    }

    private void validateUserAccess(final Id<User> userId,
                                    final Playlist playlist) {
        if (!userId.equals(playlist.getUserId()))
            throw new NoAccessToResourceException("The authenticated user doesn't have access to this playlist.");
    }

    private PlaylistItem findAndValidatePlaylistItem(final Id<Playlist> playlistId,
                                                     final Id<Title> titleId) {
        return this.repository.findByPlaylistIdAndTitleId(playlistId, titleId)
                .orElseThrow(() -> new PlaylistItemNotFoundException("This title is not in the playlist."));
    }
}
