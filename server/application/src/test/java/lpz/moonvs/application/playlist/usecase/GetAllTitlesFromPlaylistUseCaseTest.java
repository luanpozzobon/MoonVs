package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.GetAllTitlesFromPlaylistCommand;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllTitlesFromPlaylistUseCaseTest {

    @Mock
    private IPlaylistRepository playlistRepository;

    @Mock
    private IPlaylistItemRepository repository;

    @InjectMocks
    private GetAllTitlesFromPlaylistUseCase useCase;

    @Test
    void shouldExecuteSuccessfully() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, "Playlist", "Description");

        when(this.playlistRepository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        assertDoesNotThrow(() -> this.useCase.execute(new GetAllTitlesFromPlaylistCommand(userId, playlistId, 1)));
    }

    @Test
    void shouldThrowPlaylistNotFoundException() {
        when(this.playlistRepository.findById(any(Id.class))).thenReturn(Optional.empty());

        final var command = new GetAllTitlesFromPlaylistCommand(Id.unique(), Id.unique(), 1);
        final var exception = assertThrows(PlaylistNotFoundException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals(PlaylistNotFoundException.ERROR_KEY, exception.getMessage());
    }

    @Test
    void shouldThrowNoAccessToResourceWhenUserIdIsDifferent() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, "Playlist", "Description");

        when(this.playlistRepository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final var command = new GetAllTitlesFromPlaylistCommand(Id.unique(), playlistId, 1);
        final var exception = assertThrows(NoAccessToResourceException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals(NoAccessToResourceException.ERROR_KEY, exception.getMessage());
    }
}
