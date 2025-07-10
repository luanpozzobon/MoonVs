package lpz.moonvs.application.title.usecase;

import lpz.moonvs.application.title.SearchProvider;
import lpz.moonvs.application.title.command.SearchTitleCommand;
import lpz.moonvs.domain.language.contracts.ILanguageRepository;
import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.contracts.ITMDbSearchService;
import lpz.moonvs.domain.title.contracts.ITitleRepository;
import lpz.moonvs.domain.title.contracts.model.TitleSearchQuery;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.domain.title.entity.TitleTranslation;
import lpz.moonvs.domain.title.entity.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchTitleUseCaseTest {
    private static final int ID = 1;
    private static final int TMDB_ID = 1;
    private static final String TITLE = "Title";
    private static final Type TYPE = Type.MOVIE;
    private static final String OVERVIEW = "Overview";
    private static final String POSTER = "poster_path.jpg";

    @Mock
    private ITitleRepository repository;

    @Mock
    private ILanguageRepository languageRepository;

    @Mock
    private ITMDbSearchService searchService;

    @InjectMocks
    private SearchTitleUseCase useCase;

    @Test
    void shouldSearchInternally() {
        final var language = Language.load(Id.from(1), "US", "en");
        final var translation = TitleTranslation.fromSearch(OVERVIEW, POSTER);
        final var aTitle = new Title.Builder(TMDB_ID, TITLE, TYPE)
                .id(Id.from(ID))
                .translations(List.of(translation))
                .build();
        when(this.languageRepository.findByName(anyString())).thenReturn(language);
        when(this.repository.search(any(TitleSearchQuery.class))).thenReturn(List.of(aTitle));
        when(this.repository.getTotalPagesFromSearch(any(TitleSearchQuery.class))).thenReturn(1);

        final var provider = SearchProvider.valueOf("INTERNAL");
        final var command = new SearchTitleCommand("tit", "en-US", provider, 1);

        final var output = assertDoesNotThrow(() ->
                this.useCase.execute(command)
        );

        assertNotNull(output);
        assertNotNull(output.titles());
        assertFalse(output.titles().isEmpty());
        assertEquals(1, output.titles().size());
        assertNotNull(output.titles().getFirst());

        final var title = output.titles().getFirst();
        assertEquals(ID, title.id());
        assertEquals(TITLE, title.title());
        assertEquals(POSTER, title.poster());
        assertEquals(OVERVIEW, title.overview());
        assertEquals(TYPE, title.type());

        assertNotNull(output.metadata());

        assertEquals(SearchProvider.INTERNAL, output.metadata().provider());
        assertEquals(1, output.metadata().page());
        assertEquals(1, output.metadata().totalPages());
    }

    @Test
    void shouldSearchUsingTMDb() {
        final var language = Language.load(Id.from(1), "US", "en");
        final var translation = TitleTranslation.fromSearch(OVERVIEW, POSTER);
        final var aTitle = new Title.Builder(TMDB_ID, TITLE, TYPE)
                .id(Id.from(ID))
                .translations(List.of(translation))
                .build();
        when(this.languageRepository.findByName(anyString())).thenReturn(language);
        when(this.searchService.search(anyString(), anyBoolean(), any(Language.class), anyInt())).thenReturn(List.of(aTitle));

        final var provider = SearchProvider.valueOf("TMDB");
        final var command = new SearchTitleCommand("tit", "en-US", provider, 1);

        final var output = assertDoesNotThrow(() ->
                this.useCase.execute(command)
        );

        assertNotNull(output);
        assertNotNull(output.titles());
        assertFalse(output.titles().isEmpty());
        assertEquals(1, output.titles().size());
        assertNotNull(output.titles().getFirst());

        final var title = output.titles().getFirst();
        assertEquals(TMDB_ID, title.id());
        assertEquals(TITLE, title.title());
        assertEquals(POSTER, title.poster());
        assertEquals(OVERVIEW, title.overview());
        assertEquals(TYPE, title.type());

        assertNotNull(output.metadata());

        assertEquals(SearchProvider.TMDB, output.metadata().provider());
        assertEquals(1, output.metadata().page());
        assertNull(output.metadata().totalPages());
    }

    @Test
    void shouldThrowExceptionWhenProviderIsNull() {
        final var language = Language.load(Id.from(1), "US", "en");

        when(this.languageRepository.findByName(anyString())).thenReturn(language);

        final var command = new SearchTitleCommand("tit", "en-US", null, 1);

        final var exception = assertThrows(IllegalArgumentException.class, () ->
                this.useCase.execute(command)
        );

        assertNotNull(exception);
        assertEquals("Invalid provider", exception.getMessage());
    }
}
