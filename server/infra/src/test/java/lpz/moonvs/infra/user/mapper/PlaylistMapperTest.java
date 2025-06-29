package lpz.moonvs.infra.user.mapper;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.playlist.entity.PlaylistEntity;
import lpz.moonvs.infra.playlist.mapper.PlaylistMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistMapperTest {
    private static final String aTitle = "Playlist";
    private static final String aDescription = "Description";

    @Test
    void shouldMapFromDomainSuccessfully() {
        final Id<Playlist> playlistId = Id.unique();
        final Id<User> userId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, userId, aTitle, aDescription);

        final PlaylistEntity entity = assertDoesNotThrow(() ->
                PlaylistMapper.from(playlist)
        );

        assertNotNull(entity);
        assertEquals(UUID.fromString(playlistId.getValue()), entity.getId());
        assertEquals(UUID.fromString(userId.getValue()), entity.getUserId());
        assertEquals(aTitle, entity.getTitle());
        assertEquals(aDescription, entity.getDescription());
    }

    @Test
    void shouldReturnNull() {
        final PlaylistEntity entity = assertDoesNotThrow(() ->
                PlaylistMapper.from(null)
        );

        assertNull(entity);
    }

    @Test
    void shouldMapToDomainSuccessfully() {
        final PlaylistEntity entity = new PlaylistEntity();
        final UUID playlistId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();

        entity.setId(playlistId);
        entity.setUserId(userId);
        entity.setTitle(aTitle);
        entity.setDescription(aDescription);

        final Playlist playlist = assertDoesNotThrow(() ->
                PlaylistMapper.to(entity)
        );

        assertNotNull(playlist);

        assertEquals(playlistId, UUID.fromString(playlist.getId().getValue()));
        assertEquals(userId, UUID.fromString(playlist.getUserId().getValue()));
        assertEquals(aTitle, playlist.getTitle());
        assertEquals(aDescription, playlist.getDescription());
    }
}
