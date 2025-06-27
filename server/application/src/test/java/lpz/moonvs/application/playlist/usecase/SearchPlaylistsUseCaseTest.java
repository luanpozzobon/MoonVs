package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.SearchPlaylistsCommand;
import lpz.moonvs.application.playlist.output.SearchPlaylistsOutput;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.contracts.model.PlaylistSearchQuery;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchPlaylistsUseCaseTest {

    @Mock
    private IPlaylistRepository repository;

    @InjectMocks
    private SearchPlaylistsUseCase useCase;

    @Test
    void shouldExecuteSuccessfully() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, "Playlist", "Description");

        when(this.repository.search(any(Id.class), any(PlaylistSearchQuery.class))).thenReturn(List.of(aPlaylist));

        final SearchPlaylistsOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(new SearchPlaylistsCommand(userId, "Playlist"))
        );

        assertNotNull(output);
        assert output.playlists().getFirst().title().toLowerCase().contains("Playlist".toLowerCase());
    }
}
