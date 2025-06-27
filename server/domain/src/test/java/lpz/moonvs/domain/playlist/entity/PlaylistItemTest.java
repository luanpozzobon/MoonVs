package lpz.moonvs.domain.playlist.entity;

import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaylistItemTest {
    private NotificationHandler handler;

    @BeforeEach
    public void setUp() {
        this.handler = NotificationHandler.create();
    }

    @Test
    public void shouldCreateSuccessfully() {
        final PlaylistItem item = PlaylistItem.create(this.handler, Id.unique(), Id.unique(), "TV");

        assertNotNull(item);
        assertFalse(this.handler.hasError());
    }

    @Test
    public void shouldCreateSuccessfullyWhenTypeIsTv() {
        final PlaylistItem item = PlaylistItem.create(this.handler, Id.unique(), Id.unique(), "tv");

        assertNotNull(item);
        assertFalse(this.handler.hasError());
    }

    @Test
    public void shouldCreateSuccessfullyWhenTypeIsMovie() {
        final PlaylistItem item = PlaylistItem.create(this.handler, Id.unique(), Id.unique(), "movie");

        assertNotNull(item);
        assertFalse(this.handler.hasError());
    }

    @Test
    public void shouldThrowDomainValidationExceptionWhenPlaylistIdIsNull() {
        final var exception = assertThrows(DomainValidationException.class, () ->
                PlaylistItem.create(this.handler, null, Id.unique(), "TV")
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals("playlist_id", this.handler.getErrors().getFirst().getKey());
        assertEquals("The playlist id cannot be null.", this.handler.getErrors().getFirst().getMessage());

        assertEquals("Error creating a PlaylistItem", exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    public void shouldThrowDomainValidationExceptionWhenTitleIdIsNull() {
        final var exception = assertThrows(DomainValidationException.class, () ->
                PlaylistItem.create(this.handler, Id.unique(), null, "TV")
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals("title_id", this.handler.getErrors().getFirst().getKey());
        assertEquals("The title id cannot be null.", this.handler.getErrors().getFirst().getMessage());

        assertEquals("Error creating a PlaylistItem", exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "BOOKS", "book"})
    public void shouldThrowDomainValidationExceptionWhenTypeIsInvalid(String invalidType) {
        final var exception = assertThrows(DomainValidationException.class, () ->
                PlaylistItem.create(this.handler, Id.unique(), Id.unique(), invalidType)
        );

        assertTrue(this.handler.hasError());
        assertEquals(1, this.handler.getErrors().size());
        assertEquals("type", this.handler.getErrors().getFirst().getKey());
        assertEquals("The type can only be 'TV' or 'MOVIE'", this.handler.getErrors().getFirst().getMessage());

        assertEquals("Error creating a PlaylistItem", exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @Test
    public void shouldLoadExisting() {
        final PlaylistItem item = PlaylistItem.load(Id.unique(), Id.unique(), "TV");

        assertNotNull(item);
        assertFalse(this.handler.hasError());
    }
}
