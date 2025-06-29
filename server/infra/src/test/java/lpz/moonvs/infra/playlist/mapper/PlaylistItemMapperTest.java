package lpz.moonvs.infra.playlist.mapper;

import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.entity.PlaylistItem;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.infra.playlist.entity.PlaylistItemEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistItemMapperTest {
    private static final String TYPE = "TV";

    @Test
    void shouldMapFromDomainSuccessfully() {
        final Id<Playlist> playlistId = Id.unique();
        final Id<Title> titleId = Id.from(1L);
        final PlaylistItem playlistItem = PlaylistItem.load(playlistId, titleId, TYPE);

        final PlaylistItemEntity entity = assertDoesNotThrow(() ->
                PlaylistItemMapper.from(playlistItem)
        );

        assertNotNull(entity);
        assertEquals(UUID.fromString(playlistId.getValue()), entity.getPlaylistId());
        assertEquals(Long.parseLong(titleId.getValue()), entity.getTitleId());
        assertEquals(TYPE, entity.getType());
    }

    @Test
    void shouldReturnNull() {
        final PlaylistItemEntity entity = assertDoesNotThrow(() ->
                PlaylistItemMapper.from(null)
        );

        assertNull(entity);
    }

    @Test
    void shouldMapToDomainSuccessfully() {
        final PlaylistItemEntity entity = new PlaylistItemEntity();
        final UUID playlistId = UUID.randomUUID();
        final Long titleId = 1L;

        entity.setPlaylistId(playlistId);
        entity.setTitleId(titleId);
        entity.setType(TYPE);

        final PlaylistItem playlistItem = assertDoesNotThrow(() ->
                PlaylistItemMapper.to(entity)
        );

        assertNotNull(playlistItem);
        assertEquals(playlistId, UUID.fromString(playlistItem.getPlaylistId().getValue()));
        assertEquals(titleId, Long.parseLong(playlistItem.getTitleId().getValue()));
        assertEquals(TYPE, playlistItem.getType());
    }
}
