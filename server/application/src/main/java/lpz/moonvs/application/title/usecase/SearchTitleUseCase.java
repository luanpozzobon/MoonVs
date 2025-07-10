package lpz.moonvs.application.title.usecase;

import lpz.moonvs.application.title.command.SearchTitleCommand;
import lpz.moonvs.application.title.output.SearchTitleOutput;
import lpz.moonvs.domain.language.contracts.ILanguageRepository;
import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.title.contracts.ITMDbSearchService;
import lpz.moonvs.domain.title.contracts.ITitleRepository;
import lpz.moonvs.domain.title.contracts.model.TitleSearchQuery;
import lpz.moonvs.domain.title.entity.Title;

import java.util.List;

public class SearchTitleUseCase {
    private final ITitleRepository repository;
    private final ILanguageRepository languageRepository;
    private final ITMDbSearchService tmdbSearchService;

    public SearchTitleUseCase(final ITitleRepository repository,
                              final ILanguageRepository languageRepository,
                              final ITMDbSearchService tmdbSearchService) {
        this.repository = repository;
        this.languageRepository = languageRepository;
        this.tmdbSearchService = tmdbSearchService;
    }

    public SearchTitleOutput execute(final SearchTitleCommand command) {
        final Language language = this.languageRepository.findByName(command.language());

        switch (command.provider()) {
            case INTERNAL -> {
                return this.searchInternal(command, language);
            }
            case TMDB -> {
                return searchTmdb(command, language);
            }
            case null, default -> {
                throw new IllegalArgumentException("Invalid provider");
            }
        }
    }

    private SearchTitleOutput searchInternal(final SearchTitleCommand command,
                                             final Language language) {
        final var query = new TitleSearchQuery(command.title(), language.getId(), command.page());

        final List<Title> titles = this.repository.search(query);
        final int totalPages = this.repository.getTotalPagesFromSearch(query);

        return new SearchTitleOutput(
                titles.stream().map(title -> new SearchTitleOutput.Data(
                        Integer.parseInt(title.getId().getValue()),
                        title.getTitle(),
                        title.getTranslations().getFirst().getPoster(),
                        title.getTranslations().getFirst().getOverview(),
                        title.getType())
                ).toList(),
                new SearchTitleOutput.Metadata(
                        command.provider(), command.page(), totalPages
                )
        );
    }

    private SearchTitleOutput searchTmdb(final SearchTitleCommand command,
                                   final Language language) {
        final List<Title> titles = this.tmdbSearchService.search(command.title(), Boolean.TRUE, language, command.page());
        return new SearchTitleOutput(
                titles.stream().map(title -> new SearchTitleOutput.Data(
                        title.getTmdbId(),
                        title.getTitle(),
                        title.getTranslations().getFirst().getPoster(),
                        title.getTranslations().getFirst().getOverview(),
                        title.getType())
                ).toList(),
                new SearchTitleOutput.Metadata(
                        command.provider(), command.page(), null
                )
        );
    }
}