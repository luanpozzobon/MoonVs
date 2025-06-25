package lpz.moonvs.infra.playlist.mapper;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.playlist.entity.PlaylistEntity;

import java.util.Optional;
import java.util.UUID;

public abstract class PlaylistMapper {
    public static PlaylistEntity from(final Playlist playlist) {
        if (playlist == null) return null;

        final PlaylistEntity entity = new PlaylistEntity();
        Optional.ofNullable(playlist.getId())
                .map(id -> UUID.fromString(id.getValue()))
                .ifPresent(entity::setId);

        Optional.ofNullable(playlist.getUserId())
                .map(id -> UUID.fromString(id.getValue()))
                .ifPresent(entity::setUserId);

        entity.setTitle(playlist.getTitle());
        entity.setDescription(playlist.getDescription());

        return entity;
    }

    public static Playlist to(final PlaylistEntity entity) {
        final Id<Playlist> id = Id.from(entity.getId());
        final Id<User> userId = Id.from(entity.getUserId());

        return Playlist.load(id, userId, entity.getTitle(), entity.getDescription());
    }
}
