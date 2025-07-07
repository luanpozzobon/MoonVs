package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.DeletePlaylistCommand;
import lpz.moonvs.domain.auth.entity.User;
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
class DeletePlaylistUseCaseTest {
    private static final String VALID_TITLE = "Playlist";

    @Mock
    private IPlaylistRepository repository;

    @InjectMocks
    private DeletePlaylistUseCase useCase;

    @Test
    void shouldExecuteSuccessfully() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, VALID_TITLE, null);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        assertDoesNotThrow(() -> this.useCase.execute(new DeletePlaylistCommand(
                userId, playlistId
        )));
    }

    @Test
    void shouldThrowPlaylistNotFoundException() {
        when(this.repository.findById(any(Id.class))).thenReturn(Optional.empty());

        final var command = new DeletePlaylistCommand(Id.unique(), Id.unique());
        final var exception = assertThrows(PlaylistNotFoundException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals(PlaylistNotFoundException.ERROR_KEY, exception.getMessage());
    }

    @Test
    void shouldThrowNoAccessToResourceExceptionWhenDifferentUserId() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, VALID_TITLE, null);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final var command = new DeletePlaylistCommand(Id.unique(), playlistId);
        final var exception = assertThrows(NoAccessToResourceException.class, () ->
                this.useCase.execute(command)
        );

        assertEquals(NoAccessToResourceException.ERROR_KEY, exception.getMessage());
    }
}
