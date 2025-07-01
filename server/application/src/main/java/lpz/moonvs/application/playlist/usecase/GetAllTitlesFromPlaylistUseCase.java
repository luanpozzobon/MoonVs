package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.GetAllTitlesFromPlaylistCommand;
import lpz.moonvs.application.playlist.output.GetAllTitlesFromPlaylistOutput;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import lpz.moonvs.domain.seedwork.valueobject.Id;

import java.util.List;

public class GetAllTitlesFromPlaylistUseCase {
    private final IPlaylistItemRepository repository;
    private final IPlaylistRepository playlistRepository;

    public GetAllTitlesFromPlaylistUseCase(final IPlaylistItemRepository repository,
                                           final IPlaylistRepository playlistRepository) {
        this.repository = repository;
        this.playlistRepository = playlistRepository;
    }

    public GetAllTitlesFromPlaylistOutput execute(final GetAllTitlesFromPlaylistCommand command) {
        final Playlist playlist = this.findAndValidatePlaylist(command.playlistId());
        validateUserAccess(command.userId(), playlist);

        final List<PlaylistItem> playlistItems = this.repository.findAllByPlaylistId(command.playlistId(), command.page());
        final int totalPages = this.repository.getTotalPagesByPlaylistId(command.playlistId());

        return GetAllTitlesFromPlaylistOutput.from(playlistItems, command.page(), totalPages);
    }

    private Playlist findAndValidatePlaylist(final Id<Playlist> playlistId) {
        return this.playlistRepository.findById(playlistId)
                .orElseThrow(PlaylistNotFoundException::new);
    }

    private void validateUserAccess(final Id<User> userId,
                                    final Playlist playlist) {
        if (!userId.equals(playlist.getUserId()))
            throw new NoAccessToResourceException();
    }
}
