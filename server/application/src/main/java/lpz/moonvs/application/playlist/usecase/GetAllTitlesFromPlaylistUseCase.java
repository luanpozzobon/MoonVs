package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.GetAllTitlesFromPlaylistCommand;
import lpz.moonvs.application.playlist.output.GetAllTitlesFromPlaylistOutput;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;

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
        if (command.playlistId().getValue() == null || command.playlistId().getValue().isBlank())
            throw new IllegalArgumentException("Invalid playlist id.");

        final Playlist playlist = this.playlistRepository.findById(command.playlistId());
        if (playlist == null || !command.userId().equals(playlist.getUserId()))
            throw new PlaylistNotFoundException("There is no playlist with the given id.");

        final List<PlaylistItem> playlistItems = this.repository.findAllByPlaylistId(command.playlistId(), command.page());
        final int totalPages = this.repository.getTotalPagesByPlaylistId(command.playlistId());

        return GetAllTitlesFromPlaylistOutput.from(playlistItems, command.page(), totalPages);
    }
}
