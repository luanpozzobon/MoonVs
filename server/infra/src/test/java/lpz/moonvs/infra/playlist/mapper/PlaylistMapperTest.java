package lpz.moonvs.infra.playlist.mapper;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.infra.playlist.entity.PlaylistEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistMapperTest {
    private static final String TITLE = "Playlist";
    private static final String DESCRIPTION = "Description";

    @Test
    void shouldMapFromDomainSuccessfully() {
        final Id<Playlist> playlistId = Id.unique();
        final Id<User> userId = Id.unique();
        final Playlist playlist = Playlist.load(playlistId, userId, TITLE, DESCRIPTION);

        final PlaylistEntity entity = assertDoesNotThrow(() ->
                PlaylistMapper.from(playlist)
        );

        assertNotNull(entity);
        assertEquals(UUID.fromString(playlistId.getValue()), entity.getId());
        assertEquals(UUID.fromString(userId.getValue()), entity.getUserId());
        assertEquals(TITLE, entity.getTitle());
        assertEquals(DESCRIPTION, entity.getDescription());
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
        entity.setTitle(TITLE);
        entity.setDescription(DESCRIPTION);

        final Playlist playlist = assertDoesNotThrow(() ->
                PlaylistMapper.to(entity)
        );

        assertNotNull(playlist);

        assertEquals(playlistId, UUID.fromString(playlist.getId().getValue()));
        assertEquals(userId, UUID.fromString(playlist.getUserId().getValue()));
        assertEquals(TITLE, playlist.getTitle());
        assertEquals(DESCRIPTION, playlist.getDescription());
    }
}
