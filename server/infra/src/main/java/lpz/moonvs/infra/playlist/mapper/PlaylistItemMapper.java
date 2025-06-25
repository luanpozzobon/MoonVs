package lpz.moonvs.infra.playlist.mapper;

import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.infra.playlist.entity.PlaylistItemEntity;

import java.util.Optional;
import java.util.UUID;

public abstract class PlaylistItemMapper {
    public static PlaylistItemEntity from(final PlaylistItem playlistItem) {
        if (playlistItem == null) return null;

        final PlaylistItemEntity entity = new PlaylistItemEntity();

        Optional.ofNullable(playlistItem.getPlaylistId())
                .map(id -> UUID.fromString(id.getValue()))
                .ifPresent(entity::setPlaylistId);

        Optional.ofNullable(playlistItem.getTitleId())
                .map(id -> Long.parseLong(id.getValue()))
                .ifPresent(entity::setTitleId);

        entity.setType(playlistItem.getType());

        return entity;
    }

    public static PlaylistItem to(final PlaylistItemEntity entity) {
        final Id<Playlist> playlistId = Id.from(entity.getPlaylistId());
        final Id<Title> titleId = Id.from(entity.getTitleId());

        return PlaylistItem.load(playlistId, titleId, entity.getType());
    }
}
