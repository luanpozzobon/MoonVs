package lpz.moonvs.application.title.usecase;

import lpz.moonvs.application.title.SearchProvider;
import lpz.moonvs.application.title.command.SelectTitleCommand;
import lpz.moonvs.application.title.output.SelectTitleOutput;
import lpz.moonvs.domain.language.contracts.ILanguageRepository;
import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.notification.NotificationHandler;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.contracts.ITMDbSearchService;
import lpz.moonvs.domain.title.contracts.ITitleEventPublisher;
import lpz.moonvs.domain.title.contracts.ITitleRepository;
import lpz.moonvs.domain.title.dto.TitleDB;
import lpz.moonvs.domain.title.dto.tmdb.TitleDetails;
import lpz.moonvs.domain.title.entity.*;
import lpz.moonvs.domain.title.exception.TitleNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectTitleUseCaseTest {
    private static final int ID = 1;
    private static final int TMDB_ID = 1;
    private static final String TITLE = "Title";
    private static final Type TYPE = Type.MOVIE;
    private static final String TAGLINE = "tagline";
    private static final String OVERVIEW = "Overview";
    private static final String POSTER = "poster_path.jpg";
    private static final int LANGUAGE_ID = 1;
    private static final int GENRE_ID = 1;
    private static final int GENRE_TMDB_ID = 1;
    private static final String GENRE_NAME = "genre_name";
    private static final LocalDate RELEASE_DATE = LocalDate.now();
    private static final int SCREEN_TIME = 120;
    private static final boolean IS_ADULT = Boolean.TRUE;
    private static final String COUNTRY_CODE = "US";
    private static final String LANGUAGE_CODE = "en";
    private static final String LANGUAGE_NAME = "en-US";

    @Mock
    private ITitleRepository repository;

    @Mock
    private ILanguageRepository languageRepository;

    @Mock
    private ITMDbSearchService tmdbSearchService;

    @Mock
    private ITitleEventPublisher eventPublisher;

    @InjectMocks
    private SelectTitleUseCase useCase;

    private static Title title;

    @BeforeAll
    static void init() {
        final Id<Title> titleId = Id.from(ID);
        final Id<Language> languageId = Id.from(LANGUAGE_ID);
        final TitleTranslation titleTranslation = TitleTranslation.load(titleId, languageId, TITLE, TAGLINE, OVERVIEW, POSTER);

        final Id<Genre> genreId = Id.from(GENRE_ID);
        final GenreTranslation genreTranslation = GenreTranslation.load(genreId, languageId, GENRE_NAME);
        final Genre genre = Genre.load(genreId, GENRE_TMDB_ID);
        genre.addTranslation(genreTranslation);

        final TitleDB titleDB = new TitleDB(titleId, TMDB_ID, TITLE, TYPE, List.of(titleTranslation), List.of(genre),
                RELEASE_DATE, SCREEN_TIME, IS_ADULT);
        title = Title.load(titleDB);
    }

    @Test
    void shouldSelectFromInternal() {
        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(title));
        final var command = new SelectTitleCommand(ID, TYPE, SearchProvider.INTERNAL, LANGUAGE_NAME);

        final SelectTitleOutput output = assertDoesNotThrow(() -> this.useCase.execute(command));

        assertNotNull(output);
        assertEquals(ID, output.id());
    }

    @Test
    void shouldSelectFromInternalComingFromTMDb() {
        when(this.repository.findByTmdbId(anyInt())).thenReturn(Optional.of(title));
        final var command = new SelectTitleCommand(TMDB_ID, TYPE, SearchProvider.TMDB, LANGUAGE_NAME);

        final SelectTitleOutput output = assertDoesNotThrow(() -> this.useCase.execute(command));

        assertNotNull(output);
        assertEquals(TMDB_ID, output.tmdbId());
    }

    @Test
    void shouldThrowExceptionWhenInternalTitleNotFound() {
        when(this.repository.findById(any(Id.class))).thenReturn(Optional.empty());
        final var command = new SelectTitleCommand(ID, TYPE, SearchProvider.INTERNAL, LANGUAGE_NAME);

        final var exception = assertThrows(TitleNotFoundException.class, () -> this.useCase.execute(command));

        assertNotNull(exception);
        assertEquals(TitleNotFoundException.ERROR_KEY, exception.getMessage());
    }

    @Test
    void shouldSelectFromTMDb() {
        when(this.repository.findByTmdbId(anyInt())).thenReturn(Optional.empty());

        final Language language = Language.load(Id.from(1), COUNTRY_CODE, LANGUAGE_CODE);
        when(this.languageRepository.findByName(anyString())).thenReturn(language);

        final var handler = NotificationHandler.create();
        final var translation = new TitleDetails.Translation(TITLE, TAGLINE, OVERVIEW, POSTER);
        final var details = new TitleDetails(TMDB_ID, TITLE, "MOVIE", LANGUAGE_ID,
                Collections.emptyList(), translation, RELEASE_DATE, IS_ADULT, SCREEN_TIME);
        final Title aTitle = Title.fromDetails(handler, details);

        when(this.tmdbSearchService.findDetailedTitle(anyInt(), any(Type.class), any(Language.class))).thenReturn(Optional.of(aTitle));
        when(this.repository.save(any(Title.class))).thenReturn(title);

        final var command = new SelectTitleCommand(ID, TYPE, SearchProvider.TMDB, LANGUAGE_NAME);

        final SelectTitleOutput output = assertDoesNotThrow(() -> this.useCase.execute(command));

        assertNotNull(output);
        assertEquals(TMDB_ID, output.tmdbId());

        assertNotNull(output.id());
    }

    @Test
    void shouldThrowExceptionWhenTMDbTitleNotFound() {
        when(this.repository.findByTmdbId(anyInt())).thenReturn(Optional.empty());

        final Language language = Language.load(Id.from(1), COUNTRY_CODE, LANGUAGE_CODE);
        when(this.languageRepository.findByName(anyString())).thenReturn(language);

        when(this.tmdbSearchService.findDetailedTitle(anyInt(), any(Type.class), any(Language.class))).thenReturn(Optional.empty());
        final var command = new SelectTitleCommand(ID, TYPE, SearchProvider.TMDB, LANGUAGE_NAME);

        final var exception = assertThrows(TitleNotFoundException.class, () -> this.useCase.execute(command));

        assertNotNull(exception);
        assertEquals(TitleNotFoundException.ERROR_KEY, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSearchProviderIsNull() {
        final var command = new SelectTitleCommand(ID, TYPE, null, LANGUAGE_NAME);

        final var exception = assertThrows(IllegalArgumentException.class, () -> this.useCase.execute(command));

        assertNotNull(exception);
        assertEquals("Invalid provider", exception.getMessage());
    }
}
