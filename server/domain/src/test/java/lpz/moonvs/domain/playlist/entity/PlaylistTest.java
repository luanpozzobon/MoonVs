package lpz.moonvs.domain.playlist.entity;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.validation.PlaylistValidator;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    private Id<User> userId;
    private NotificationHandler handler;

    @BeforeEach
    void setUp() {
        this.userId = Id.from("valid_id");
        this.handler = NotificationHandler.create();
    }

    @Test
    void shouldCreatePlaylist() {
        final Playlist playlist = assertDoesNotThrow(() ->
                Playlist.create(this.handler, this.userId, "Playlist", "")
        );

        assertNotNull(playlist);
        assertNotNull(playlist.getId());

        assertEquals(this.userId, playlist.getUserId());

        assertFalse(this.handler.hasError());
    }

    @Test
    void shouldThrowDomainValidationExceptionWhenUserIdIsNull() {
        final var exception = assertThrows(DomainValidationException.class, () ->
                Playlist.create(this.handler, null, "Playlist", null)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(Playlist.USER_ID_KEY, this.handler.getErrors().getFirst().getKey());
        assertEquals(PlaylistValidator.NULL_OR_BLANK_KEY, this.handler.getErrors().getFirst().getMessage());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    void shouldThrowDomainValidationExceptionWhenTitleIsNullOrBlank(String invalidTitle) {
        final var exception = assertThrows(DomainValidationException.class, () ->
                Playlist.create(this.handler, this.userId, invalidTitle, null)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(Playlist.TITLE_KEY, this.handler.getErrors().getFirst().getKey());
        assertEquals(PlaylistValidator.NULL_OR_BLANK_KEY, this.handler.getErrors().getFirst().getMessage());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldThrowDomainValidationExceptionWhenTitleIsTooLarge() {
        final String title = "a".repeat(65);
        final var exception = assertThrows(DomainValidationException.class, () ->
                Playlist.create(this.handler, this.userId, title, null)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(Playlist.TITLE_KEY, this.handler.getErrors().getFirst().getKey());
        assertEquals(PlaylistValidator.MAXIMUM_LENGTH_KEY, this.handler.getErrors().getFirst().getMessage());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldThrowDomainValidationExceptionWhenDescriptionIsTooLarge() {
        final String description = "a".repeat(256);
        final var exception = assertThrows(DomainValidationException.class, () ->
                Playlist.create(this.handler, this.userId, "Playlist", description)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(Playlist.DESCRIPTION_KEY, this.handler.getErrors().getFirst().getKey());
        assertEquals(PlaylistValidator.MAXIMUM_LENGTH_KEY, this.handler.getErrors().getFirst().getMessage());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldRename() {
        final Playlist playlist = Playlist.create(this.handler, this.userId, "Playlist", null);
        playlist.rename(this.handler, "Watchlist");

        assertEquals("Watchlist", playlist.getTitle());
    }

    @Test
    void shouldChangeDescription() {
        final Playlist playlist = Playlist.create(this.handler, this.userId, "Playlist", null);
        playlist.updateDescription(this.handler, "New Description");

        assertEquals("New Description", playlist.getDescription());
    }

    @Test
    void shouldLoadExisting() {
        final Playlist playlist = Playlist.load(Id.unique(), Id.unique(), "Playlist", null);

        assertNotNull(playlist);
        assertFalse(this.handler.hasError());
    }
}
