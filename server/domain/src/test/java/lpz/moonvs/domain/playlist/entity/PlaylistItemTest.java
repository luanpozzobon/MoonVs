package lpz.moonvs.domain.playlist.entity;

import lpz.moonvs.domain.playlist.validation.PlaylistItemValidator;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistItemTest {
    private NotificationHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = NotificationHandler.create();
    }

    @ParameterizedTest
    @ValueSource(strings = {"TV", "tv", "Tv", "MOVIE", "movie", "Movie"})
    void shouldCreateSuccessfully(final String type) {
        final PlaylistItem item = PlaylistItem.create(this.handler, Id.unique(), Id.unique(), type);

        assertNotNull(item);
        assertFalse(this.handler.hasError());
    }

    @Test
    void shouldThrowDomainValidationExceptionWhenPlaylistIdIsNull() {
        final Id<Title> titleId = Id.unique();
        final var exception = assertThrows(DomainValidationException.class, () ->
                PlaylistItem.create(this.handler, null, titleId, "TV")
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(PlaylistItem.PLAYLIST_ID_KEY, this.handler.getErrors().getFirst().getKey());
        assertEquals(PlaylistItemValidator.NULL_OR_BLANK_KEY, this.handler.getErrors().getFirst().getMessage());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldThrowDomainValidationExceptionWhenTitleIdIsNull() {
        final Id<Playlist> playlistId = Id.unique();
        final var exception = assertThrows(DomainValidationException.class, () ->
                PlaylistItem.create(this.handler, playlistId, null, "TV")
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(PlaylistItem.TITLE_ID_KEY, this.handler.getErrors().getFirst().getKey());
        assertEquals(PlaylistItemValidator.NULL_OR_BLANK_KEY, this.handler.getErrors().getFirst().getMessage());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "BOOKS", "book"})
    void shouldThrowDomainValidationExceptionWhenTypeIsInvalid(final String invalidType) {
        final Id<Playlist> playlistId = Id.unique();
        final Id<Title> titleId = Id.unique();
        final var exception = assertThrows(DomainValidationException.class, () ->
                PlaylistItem.create(this.handler, playlistId, titleId, invalidType)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(PlaylistItem.TYPE_KEY, this.handler.getErrors().getFirst().getKey());
        assertEquals(PlaylistItemValidator.INVALID_TYPE_KEY, this.handler.getErrors().getFirst().getMessage());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldLoadExisting() {
        final PlaylistItem item = PlaylistItem.load(Id.unique(), Id.unique(), "TV");

        assertNotNull(item);
        assertFalse(this.handler.hasError());
    }
}
