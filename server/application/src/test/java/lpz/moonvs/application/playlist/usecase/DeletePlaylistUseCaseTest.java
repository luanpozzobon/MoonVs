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
public class DeletePlaylistUseCaseTest {
    @Mock
    private IPlaylistRepository repository;

    @InjectMocks
    private DeletePlaylistUseCase useCase;

    @Test
    public void shouldExecuteSuccessfully() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, null, null);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        assertDoesNotThrow(() -> this.useCase.execute(new DeletePlaylistCommand(
                userId, playlistId
        )));
    }

    @Test
    public void shouldThrowPlaylistNotFoundException() {
        when(this.repository.findById(any(Id.class))).thenReturn(Optional.empty());

        final var exception = assertThrows(PlaylistNotFoundException.class, () ->
                this.useCase.execute(new DeletePlaylistCommand(Id.unique(), Id.unique()))
        );

        assertEquals("There is no playlist with the given id.", exception.getMessage());
    }

    @Test
    public void shouldThrowNoAccessToResourceExceptionWhenDifferentUserId() {
        final Id<User> userId = Id.unique();
        final Id<Playlist> playlistId = Id.unique();
        final Playlist aPlaylist = Playlist.load(playlistId, userId, null, null);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final var exception = assertThrows(NoAccessToResourceException.class, () ->
                this.useCase.execute(new DeletePlaylistCommand(Id.unique(), playlistId))
        );

        assertEquals("The authenticated user doesn't have access to this playlist.", exception.getMessage());
    }
}
