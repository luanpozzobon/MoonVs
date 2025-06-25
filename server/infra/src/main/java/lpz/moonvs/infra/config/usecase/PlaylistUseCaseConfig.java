package lpz.moonvs.infra.config.usecase;

import lpz.moonvs.application.playlist.usecase.*;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.title.contracts.ITitleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaylistUseCaseConfig {
    @Bean
    public CreatePlaylistUseCase createPlaylistUseCase(final IPlaylistRepository repository) {
        return new CreatePlaylistUseCase(repository);
    }

    @Bean
    public GetAllPlaylistsUseCase getAllPlaylistsUseCase(final IPlaylistRepository repository) {
        return new GetAllPlaylistsUseCase(repository);
    }

    @Bean
    public GetPlaylistByIdUseCase getPlaylistByIdUseCase(final IPlaylistRepository repository) {
        return new GetPlaylistByIdUseCase(repository);
    }

    @Bean
    public SearchPlaylistsUseCase searchPlaylistsUseCase(final IPlaylistRepository repository) {
        return new SearchPlaylistsUseCase(repository);
    }

    @Bean
    public UpdatePlaylistUseCase updatePlaylistUseCase(final IPlaylistRepository repository) {
        return new UpdatePlaylistUseCase(repository);
    }

    @Bean
    public DeletePlaylistUseCase deletePlaylistUseCase(final IPlaylistRepository repository) {
        return new DeletePlaylistUseCase(repository);
    }

    @Bean
    public AddTitleToPlaylistUseCase addTitleToPlaylistUseCase(final IPlaylistItemRepository repository,
                                                               final IPlaylistRepository playlistRepository,
                                                               final ITitleRepository titleRepository) {
        return new AddTitleToPlaylistUseCase(repository, playlistRepository, titleRepository);
    }

    @Bean
    public RemoveTitleFromPlaylistUseCase removeTitleFromPlaylistUseCase(final IPlaylistItemRepository repository,
                                                                         final IPlaylistRepository playlistRepository) {
        return new RemoveTitleFromPlaylistUseCase(repository, playlistRepository);
    }

    @Bean
    public GetAllTitlesFromPlaylistUseCase getAllTitlesFromPlaylistUseCase(final IPlaylistItemRepository repository,
                                                                           final IPlaylistRepository playlistRepository) {
        return new GetAllTitlesFromPlaylistUseCase(repository, playlistRepository);
    }
}
