package lpz.moonvs.domain.title.entity;

import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTranslationTest {
    private static final int LANGUAGE_ID = 1;
    private static final String NAME = "name";
    private static final int ID = 1;


    @Test
    void shouldCreate() {
        final Id<Language> languageId = Id.from(LANGUAGE_ID);
        final var translation = assertDoesNotThrow(() ->
                GenreTranslation.create(languageId, NAME)
        );

        assertNotNull(translation);
        assertNull(translation.getGenreId());

        assertEquals(LANGUAGE_ID, Integer.parseInt(translation.getLanguageId().getValue()));
        assertEquals(NAME, translation.getName());
    }

    @Test
    void shouldLoad() {
        final Id<Genre> genreId = Id.from(ID);
        final Id<Language> languageId = Id.from(LANGUAGE_ID);

        final var translation = assertDoesNotThrow(() ->
                GenreTranslation.load(genreId, languageId, NAME)
        );

        assertNotNull(translation);
        assertNotNull(translation.getGenreId());

        assertEquals(ID, Integer.parseInt(translation.getGenreId().getValue()));
        assertEquals(LANGUAGE_ID, Integer.parseInt(translation.getLanguageId().getValue()));
        assertEquals(NAME, translation.getName());
    }
}
