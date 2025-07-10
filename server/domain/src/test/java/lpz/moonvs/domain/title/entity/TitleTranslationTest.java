package lpz.moonvs.domain.title.entity;

import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.dto.tmdb.TitleDetails;
import lpz.moonvs.domain.title.dto.tmdb.Translations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TitleTranslationTest {
    private static final String OVERVIEW = "overview";
    private static final String POSTER = "poster_oath.png";
    private static final int LANGUAGE_ID = 1;
    private static final String TITLE = "title";
    private static final String TAGLINE = "tagline";
    private static final int TITLE_ID = 1;

    @Test
    void shouldCreateFromSearch() {
        final var translation = assertDoesNotThrow(() ->
                TitleTranslation.fromSearch(OVERVIEW, POSTER)
        );

        assertNotNull(translation);
        assertEquals(OVERVIEW, translation.getOverview());
        assertEquals(POSTER, translation.getPoster());
    }

    @Test
    void shouldCreateFromDetails() {
        final Id<Language> languageId = Id.from(LANGUAGE_ID);
        final var translationDetail = new TitleDetails.Translation(TITLE, TAGLINE, OVERVIEW, POSTER);

        final var translation = assertDoesNotThrow(() ->
                TitleTranslation.fromDetails(languageId, translationDetail)
        );

        assertNotNull(translation);
        assertEquals(LANGUAGE_ID, Integer.parseInt(translation.getLanguageId().getValue()));
        assertEquals(TITLE, translation.getTitle());
        assertEquals(TAGLINE, translation.getTagline());
        assertEquals(OVERVIEW, translation.getOverview());
        assertEquals(POSTER, translation.getPoster());
    }

    @Test
    void shouldCreateFromTranslation() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final Id<Language> languageId = Id.from(LANGUAGE_ID);
        final var data = new Translations.Data(OVERVIEW, TAGLINE, TITLE);
        final var translations = new Translations.Translation(null, null, data);

        final var translation = assertDoesNotThrow(()->
                TitleTranslation.fromTranslation(titleId, languageId, translations)
        );

        assertNotNull(translation);

        assertEquals(TITLE_ID, Integer.parseInt(translation.getTitleId().getValue()));
        assertEquals(LANGUAGE_ID, Integer.parseInt(translation.getLanguageId().getValue()));
        assertEquals(OVERVIEW, translation.getOverview());
        assertEquals(TAGLINE, translation.getTagline());
        assertEquals(TITLE, translation.getTitle());
    }

    @Test
    void shouldLoad() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final Id<Language> languageId = Id.from(LANGUAGE_ID);

        final var translation = assertDoesNotThrow(() ->
                TitleTranslation.load(titleId, languageId, TITLE, TAGLINE, OVERVIEW, POSTER)
        );

        assertNotNull(translation);

        assertEquals(TITLE_ID, Integer.parseInt(translation.getTitleId().getValue()));
        assertEquals(LANGUAGE_ID, Integer.parseInt(translation.getLanguageId().getValue()));
        assertEquals(OVERVIEW, translation.getOverview());
        assertEquals(TAGLINE, translation.getTagline());
        assertEquals(TITLE, translation.getTitle());
        assertEquals(POSTER, translation.getPoster());
    }

    @Test
    void shouldAddPoster() {
        final Id<Title> titleId = Id.from(TITLE_ID);
        final Id<Language> languageId = Id.from(LANGUAGE_ID);
        final var translation = assertDoesNotThrow(() ->
                TitleTranslation.load(titleId, languageId, TITLE, TAGLINE, OVERVIEW, POSTER)
        );

        final String newPoster = "new_poster.png";

        translation.addPoster(newPoster);

        assertNotNull(translation.getPoster());
        assertEquals(newPoster, translation.getPoster());
    }
}
