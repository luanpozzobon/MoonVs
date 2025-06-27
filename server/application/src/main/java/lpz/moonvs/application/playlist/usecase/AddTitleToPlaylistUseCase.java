package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.AddTitleToPlaylistCommand;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.contracts.ITitleRepository;
import lpz.moonvs.domain.title.entity.Title;

public class AddTitleToPlaylistUseCase {
    private final IPlaylistItemRepository repository;
    private final IPlaylistRepository playlistRepository;
    private final ITitleRepository titleRepository;

    public AddTitleToPlaylistUseCase(final IPlaylistItemRepository repository,
                                     final IPlaylistRepository playlistRepository,
                                     final ITitleRepository titleRepository) {
        this.repository = repository;
        this.playlistRepository = playlistRepository;
        this.titleRepository = titleRepository;
    }

    public void execute(final AddTitleToPlaylistCommand command) {
        final Playlist playlist = this.findAndValidatePlaylist(command.playlistId());
        this.validateUserAccess(command.userId(), playlist);

        PlaylistItem playlistItem = this.findAndValidatePlaylistItem(command.playlistId(), command.titleId());
        if (playlistItem != null)
            return;

        final NotificationHandler handler = NotificationHandler.create();
        playlistItem = PlaylistItem.create(handler, command.playlistId(), command.titleId(), command.type());

        this.repository.save(playlistItem);
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
                .orElse(null);
    }
}
