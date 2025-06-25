package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.AddTitleToPlaylistCommand;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.playlist.exception.PlaylistItemAlreadyExistsException;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.title.contracts.ITitleRepository;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.domain.title.exception.TitleNotFoundException;

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
        if (command.playlistId().getValue() == null || command.playlistId().getValue().isBlank())
            throw new IllegalArgumentException("Invalid playlist id.");

        final Playlist playlist = this.playlistRepository.findById(command.playlistId());
        if (playlist == null || !command.userId().equals(playlist.getUserId()))
            throw new PlaylistNotFoundException("There is no playlist with the given id.");

//        final Title title = this.titleRepository.findById(command.titleId());
//        if (title == null)
//            throw new TitleNotFoundException("There is no title with the given id.");

        final PlaylistItem anPlaylistItem = this.repository.findByPlaylistIdAndTitleId(command.playlistId(), command.titleId());
        if (anPlaylistItem != null)
            return;

        final NotificationHandler handler = NotificationHandler.create();
        final PlaylistItem playlistItem = PlaylistItem.create(handler, command.playlistId(), command.titleId(), command.type());

        this.repository.save(playlistItem);
    }
}
