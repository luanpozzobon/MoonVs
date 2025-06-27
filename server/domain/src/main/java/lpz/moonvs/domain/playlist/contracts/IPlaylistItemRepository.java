package lpz.moonvs.domain.playlist.contracts;

import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;

import java.util.List;
import java.util.Optional;

public interface IPlaylistItemRepository {
    PlaylistItem save(final PlaylistItem playlistItem);

    Optional<PlaylistItem> findByPlaylistIdAndTitleId(final Id<Playlist> playlistId, final Id<Title> titleId);

    List<PlaylistItem> findAllByPlaylistId(final Id<Playlist> playlistId, final Integer page);

    int getTotalPagesByPlaylistId(final Id<Playlist> playlistId);

    int delete(final PlaylistItem playlistItem);
}
