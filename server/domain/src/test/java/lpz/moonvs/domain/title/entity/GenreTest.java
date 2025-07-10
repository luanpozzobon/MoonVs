package lpz.moonvs.domain.title.entity;

import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.dto.tmdb.TitleDetails;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {
    private static final int VALID_TMDB_ID = 1;
    private static final String VALID_NAME = "name";
    private static final int VALID_LANGUAGE_ID = 1;
    private static final int VALID_ID = 1;

    @Test
    void shouldCreateFromDetails() {
        final var genreDetails = new TitleDetails.Genre(VALID_TMDB_ID, VALID_NAME);

        final var genre = assertDoesNotThrow(() ->
                Genre.fromDetails(VALID_LANGUAGE_ID, genreDetails)
        );

        assertNotNull(genre);
    }

    @Test
    void shouldLoad() {
        final Id<Genre> id = Id.from(VALID_ID);

        final var genre = assertDoesNotThrow(() ->
                Genre.load(id, VALID_TMDB_ID)
        );

        assertNotNull(genre);
    }

    @Test
    void shouldSetTranslations() {
        final Id<Genre> id = Id.from(VALID_ID);
        final var genre = assertDoesNotThrow(() ->
                Genre.load(id, VALID_TMDB_ID)
        );
        final List<GenreTranslation> translations = List.of(GenreTranslation.create(Id.from(VALID_LANGUAGE_ID), VALID_NAME));

        genre.setTranslations(translations);

        assertNotNull(genre.getTranslations());
        assertFalse(genre.getTranslations().isEmpty());
        assertEquals(translations, genre.getTranslations());
    }

    @Test
    void shouldAddTranslation() {
        final Id<Language> languageId = Id.from(VALID_LANGUAGE_ID);
        final Id<Genre> id = Id.from(VALID_ID);
        final var genre = assertDoesNotThrow(() ->
                Genre.load(id, VALID_TMDB_ID)
        );
        final GenreTranslation translation = GenreTranslation.create(languageId, VALID_NAME);

        genre.addTranslation(translation);

        assertNotNull(genre.getTranslations());
        assertFalse(genre.getTranslations().isEmpty());
        assertEquals(translation, genre.getTranslations().getFirst());
    }
}
