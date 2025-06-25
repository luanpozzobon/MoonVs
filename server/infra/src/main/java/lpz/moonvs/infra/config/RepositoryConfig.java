package lpz.moonvs.infra.config;

import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.title.contracts.ITitleRepository;
import lpz.moonvs.infra.auth.repository.UserRepository;
import lpz.moonvs.infra.playlist.repository.PlaylistItemRepository;
import lpz.moonvs.infra.playlist.repository.PlaylistRepository;
import lpz.moonvs.infra.title.repository.TitleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfig {

    @Bean
    public IUserRepository userRepository(final DataSource dataSource) {
        return new UserRepository(dataSource);
    }

    @Bean
    public IPlaylistRepository playlistRepository(final DataSource dataSource) {
        return new PlaylistRepository(dataSource);
    }

    @Bean
    public IPlaylistItemRepository playlistItemRepository(final DataSource dataSource) {
        return new PlaylistItemRepository(dataSource);
    }

    @Bean
    public ITitleRepository titleRepository(final DataSource dataSource) {
        return new TitleRepository(dataSource);
    }
}
