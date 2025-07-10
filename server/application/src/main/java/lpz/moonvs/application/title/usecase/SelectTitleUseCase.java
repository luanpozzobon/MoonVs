package lpz.moonvs.application.title.usecase;

import lpz.moonvs.application.title.command.SelectTitleCommand;
import lpz.moonvs.application.title.output.SelectTitleOutput;
import lpz.moonvs.domain.language.contracts.ILanguageRepository;
import lpz.moonvs.domain.language.entity.Language;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.contracts.ITMDbSearchService;
import lpz.moonvs.domain.title.contracts.ITitleEventPublisher;
import lpz.moonvs.domain.title.contracts.ITitleRepository;
import lpz.moonvs.domain.title.entity.Title;
import lpz.moonvs.domain.title.exception.TitleNotFoundException;

import java.util.Optional;

public class SelectTitleUseCase {
    private static final String INVALID_PROVIDER = "Invalid provider";

    private final ITitleRepository repository;
    private final ILanguageRepository languageRepository;
    private final ITMDbSearchService tmdbSearchService;
    private final ITitleEventPublisher eventPublisher;

    public SelectTitleUseCase(final ITitleRepository repository,
                              final ILanguageRepository languageRepository,
                              final ITMDbSearchService tmdbSearchService,
                              final ITitleEventPublisher eventPublisher) {
        this.repository = repository;
        this.languageRepository = languageRepository;
        this.tmdbSearchService = tmdbSearchService;
        this.eventPublisher = eventPublisher;
    }

    public SelectTitleOutput execute(final SelectTitleCommand command) {
        Optional<Title> foundTitle = findInternally(command);

        if (foundTitle.isPresent())
            return SelectTitleOutput.from(foundTitle.get());

        foundTitle = this.findExternally(command);
        if (foundTitle.isEmpty())
            throw new TitleNotFoundException();

        Title title = foundTitle.get();

        title = this.repository.save(title);
        this.eventPublisher.publishTitleCreated(title.getId());

        return SelectTitleOutput.from(title);
    }

    private Optional<Title> findInternally(final SelectTitleCommand command) {
        switch (command.provider()) {
            case INTERNAL -> {
                Optional<Title> optTitle = this.repository.findById(Id.from(command.id()));
                if (optTitle.isEmpty())
                    throw new TitleNotFoundException();

                return optTitle;
            }
            case TMDB -> {
                return this.repository.findByTmdbId(command.id());
            }
            case null, default -> throw new IllegalArgumentException(INVALID_PROVIDER);
        }
    }

    private Optional<Title> findExternally(final SelectTitleCommand command) {
        final Language language = this.languageRepository.findByName(command.language());

        switch (command.provider()) {
            case TMDB -> {
                return this.tmdbSearchService.findDetailedTitle(command.id(), command.type(), language);
            }
            case null, default -> throw new IllegalArgumentException(INVALID_PROVIDER);
        }
    }
}
