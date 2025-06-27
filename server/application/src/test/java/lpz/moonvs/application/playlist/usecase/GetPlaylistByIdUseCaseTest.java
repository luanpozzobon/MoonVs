package lpz.moonvs.application.playlist.usecase;

import lpz.moonvs.application.playlist.command.GetPlaylistByIdCommand;
import lpz.moonvs.application.playlist.output.GetPlaylistByIdOutput;
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
public class GetPlaylistByIdUseCaseTest {
    private static final String VALID_TITLE = "Playlist";
    private static final String VALID_DESCRIPTION = "Description";

    @Mock
    private IPlaylistRepository repository;

    @InjectMocks
    private GetPlaylistByIdUseCase useCase;

    @Test
    public void shouldExecuteSuccessfully() {
        final Id<User> userId = Id.unique();
        final Playlist aPlaylist = Playlist.load(Id.unique(), userId, VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final GetPlaylistByIdOutput output = assertDoesNotThrow(() ->
                this.useCase.execute(new GetPlaylistByIdCommand(userId, Id.unique()))
        );

        assertNotNull(output);
        assertEquals(VALID_TITLE, output.title());
        assertEquals(VALID_DESCRIPTION, output.description());
    }

    @Test
    public void shouldThrowPlaylistNotFoundExceptionWhenIdDontExists() {
        when(this.repository.findById(any(Id.class))).thenReturn(Optional.empty());

        final var exception = assertThrows(PlaylistNotFoundException.class, () ->
                this.useCase.execute(new GetPlaylistByIdCommand(Id.unique(), Id.unique()))
        );

        assertEquals("There is no playlist with the given id.", exception.getMessage());
    }

    @Test
    public void shouldThrowNoAccessToResourceExceptionWhenDifferentUserId() {
        final Id<User> userId = Id.unique();
        final Playlist aPlaylist = Playlist.load(Id.unique(), userId, VALID_TITLE, VALID_DESCRIPTION);

        when(this.repository.findById(any(Id.class))).thenReturn(Optional.of(aPlaylist));

        final var exception = assertThrows(NoAccessToResourceException.class, () ->
                this.useCase.execute(new GetPlaylistByIdCommand(Id.unique(), Id.unique()))
        );

        assertEquals("The authenticated user doesn't have access to this playlist.", exception.getMessage());
    }
}
