package lpz.moonvs.domain.title.entity;

import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.notification.Notification;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.dto.TitleDB;
import lpz.moonvs.domain.title.dto.tmdb.TitleDetails;
import lpz.moonvs.domain.title.dto.tmdb.TitleSearch;
import lpz.moonvs.domain.title.entity.schema.TitleSchema;
import lpz.moonvs.domain.title.validation.TitleValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TitleTest {
    private static final int VALID_ID = 1;
    private static final int VALID_TMDB_ID = 1;
    private static final String VALID_TITLE = "title";
    private static final Type VALID_TYPE = Type.MOVIE;
    private static final String OVERVIEW = "overview";
    private static final String POSTER_PATH = "poster_path.jpg";
    private static final int LANGUAGE_ID = 1;
    private static final LocalDate RELEASE_DATE = LocalDate.of(2020, 1, 1);
    private static final int SCREEN_TIME = 120;


    @Test
    void shouldCreateFromSearch() {
        final var search = new TitleSearch(VALID_TMDB_ID, VALID_TITLE, OVERVIEW, POSTER_PATH, VALID_TYPE.toString());
        final var handler = NotificationHandler.create();

        final Title title = assertDoesNotThrow(() ->
                Title.fromSearch(handler, search)
        );

        assertFalse(handler.hasError());

        assertNotNull(title);
    }

    @Test
    void shouldCreateFromDetails() {
        final var translation = new TitleDetails.Translation(VALID_TITLE, null, OVERVIEW, POSTER_PATH);
        final var genre = new TitleDetails.Genre(1, "genre");
        final var details = new TitleDetails(VALID_TMDB_ID, VALID_TITLE, VALID_TYPE.toString(), LANGUAGE_ID,
                List.of(genre), translation, RELEASE_DATE, Boolean.FALSE, SCREEN_TIME);
        final var handler = NotificationHandler.create();

        final Title title = assertDoesNotThrow(() ->
                Title.fromDetails(handler, details)
        );

        assertFalse(handler.hasError());

        assertNotNull(title);
    }

    @Test
    void shouldLoad() {
        final var titleDB = new TitleDB(Id.from(VALID_ID), VALID_TMDB_ID, VALID_TITLE, VALID_TYPE,
                Collections.emptyList(), Collections.emptyList(), RELEASE_DATE, SCREEN_TIME, Boolean.TRUE);

        final var title = assertDoesNotThrow(() ->
                Title.load(titleDB)
        );

        assertNotNull(title);
    }

    @Test
    void shouldNotCreateWithNullTMDbId() {
        final var search = new TitleSearch(null, VALID_TITLE, OVERVIEW, POSTER_PATH, VALID_TYPE.toString());
        final var handler = NotificationHandler.create();

        final var exception = assertThrows(NullPointerException.class, () ->
                Title.fromSearch(handler, search)
        );

        assertEquals("'tmdb_id' cannot be null.", exception.getMessage());
    }

    @Test
    void shouldNotCreateWithNullTitle() {
        final var search = new TitleSearch(VALID_TMDB_ID, null, OVERVIEW, POSTER_PATH, VALID_TYPE.toString());
        final var handler = NotificationHandler.create();

        final var exception = assertThrows(NullPointerException.class, () ->
                Title.fromSearch(handler, search)
        );

        assertEquals("'title' cannot be null.", exception.getMessage());
    }

    @Test
    void shouldNotLoadWithNullType() {
        final var titleDB = new TitleDB(Id.from(VALID_ID), VALID_TMDB_ID, VALID_TITLE, null, Collections.emptyList(),
                Collections.emptyList(), RELEASE_DATE, SCREEN_TIME, Boolean.FALSE);

        final var exception = assertThrows(NullPointerException.class, () ->
                Title.load(titleDB)
        );

        assertEquals("'type' cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void shouldNotCreateWithInvalidTMDbId(int invalidTMDbId) {
        final var search = new TitleSearch(invalidTMDbId, VALID_TITLE, OVERVIEW, POSTER_PATH, VALID_TYPE.toString());
        final var handler = NotificationHandler.create();

        final var exception = assertThrows(DomainValidationException.class, () ->
                Title.fromSearch(handler, search)
        );

        assertTrue(handler.hasError());
        assertEquals(1, handler.getErrors().size());
        assertEquals(TitleSchema.TMDB_ID, handler.getErrors().getFirst().key());
        assertEquals(TitleValidator.INVALID_TMDB_ID, handler.getErrors().getFirst().message());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    void shouldNotCreateWithInvalidTitle(String invalidTitle) {
        final var search = new TitleSearch(VALID_TMDB_ID, invalidTitle, OVERVIEW, POSTER_PATH, VALID_TYPE.toString());
        final var handler = NotificationHandler.create();

        final var exception = assertThrows(DomainValidationException.class, () ->
                Title.fromSearch(handler, search)
        );

        assertTrue(handler.hasError());
        assertEquals(1, handler.getErrors().size());
        assertEquals(TitleSchema.TITLE, handler.getErrors().getFirst().key());
        assertEquals(Notification.NULL_OR_BLANK, handler.getErrors().getFirst().message());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "people"})
    void shouldNotCreateWithInvalidType(String invalidType) {
        final var search = new TitleSearch(VALID_TMDB_ID, VALID_TITLE, OVERVIEW, POSTER_PATH, invalidType);
        final var handler = NotificationHandler.create();

        assertThrows(IllegalArgumentException.class, () ->
                Title.fromSearch(handler, search)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void shouldNotCreateWithInvalidScreenTime(int invalidScreenTime) {
        final var details = new TitleDetails(VALID_TMDB_ID, VALID_TITLE, VALID_TYPE.toString(), LANGUAGE_ID,
                Collections.emptyList(), new TitleDetails.Translation(VALID_TITLE, null, OVERVIEW, POSTER_PATH), RELEASE_DATE, Boolean.FALSE, invalidScreenTime);
        final var handler = NotificationHandler.create();

        final var exception = assertThrows(DomainValidationException.class, () ->
                Title.fromDetails(handler, details)
        );

        assertTrue(handler.hasError());
        assertEquals(1, handler.getErrors().size());
        assertEquals(TitleSchema.SCREEN_TIME, handler.getErrors().getFirst().key());
        assertEquals(TitleValidator.INVALID_SCREEN_TIME, handler.getErrors().getFirst().message());

        assertEquals(DomainValidationException.ERROR_KEY, exception.getMessage());
        assertNotNull(exception.getErrors());
    }
}
