package lpz.moonvs.domain.playlist.entity;

import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    private static final String VALID_TITLE = "Playlist";

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
                Playlist.create(this.handler, this.userId, VALID_TITLE, "")
        );

        assertNotNull(playlist);
        assertNotNull(playlist.getId());

        assertEquals(this.userId, playlist.getUserId());

        assertFalse(this.handler.hasError());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenUserIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                Playlist.create(this.handler, null, VALID_TITLE, null)
        );
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    void shouldThrowDomainValidationExceptionWhenTitleIsBlank(String invalidTitle) {
        final var exception = assertThrows(DomainValidationException.class, () ->
                Playlist.create(this.handler, this.userId, invalidTitle, null)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(Playlist.Schema.TITLE, this.handler.getErrors().getFirst().key());
        assertEquals(Notification.Schema.NULL_OR_BLANK, this.handler.getErrors().getFirst().message());

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
        assertEquals(Playlist.Schema.TITLE, this.handler.getErrors().getFirst().key());
        assertEquals(Notification.Schema.MAX_LENGTH, this.handler.getErrors().getFirst().message());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldThrowDomainValidationExceptionWhenDescriptionIsTooLarge() {
        final String description = "a".repeat(256);
        final var exception = assertThrows(DomainValidationException.class, () ->
                Playlist.create(this.handler, this.userId, VALID_TITLE, description)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals(Playlist.Schema.DESCRIPTION, this.handler.getErrors().getFirst().key());
        assertEquals(Notification.Schema.MAX_LENGTH, this.handler.getErrors().getFirst().message());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    void shouldRename() {
        final Playlist playlist = Playlist.create(this.handler, this.userId, VALID_TITLE, null);
        final String newTitle = "New Title";
        playlist.rename(this.handler, newTitle);

        assertEquals(newTitle, playlist.getTitle());
    }

    @Test
    void shouldChangeDescription() {
        final Playlist playlist = Playlist.create(this.handler, this.userId, VALID_TITLE, null);
        final String newDescription = "New Description";
        playlist.updateDescription(this.handler, newDescription);

        assertEquals(newDescription, playlist.getDescription());
    }

    @Test
    void shouldLoadExisting() {
        final Playlist playlist = Playlist.load(Id.unique(), Id.unique(), VALID_TITLE, null);

        assertNotNull(playlist);
        assertFalse(this.handler.hasError());
    }
}
